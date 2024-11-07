package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.persistence.enums.ExportTypeEnum;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.persistence.repository.ProcedureRepository;
import com.opti_pet.backend_app.rest.request.ExcelExportRequest;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.opti_pet.backend_app.util.AppConstants.*;

@Service
@RequiredArgsConstructor
public class ExcelExporterService {
    private final ClinicService clinicService;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final ProcedureRepository procedureRepository;

    @Transactional
    public void importProcedures(String clinicId, HttpServletResponse response, MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook1 = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet1 = workbook1.getSheetAt(0);

            Row headerRow = sheet1.getRow(0);
            if (!isValidHeader(headerRow)) {
                throw new BadRequestException("Invalid Excel format: headers do not match expected format.");
            }

            Iterator<Row> rowIterator = sheet1.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Procedure procedure = mapRowToProcedure(row);

                Procedure existingProcedure = procedureRepository.findById(procedure.getId()).orElse(null);
                if (existingProcedure == null) {
                    Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
                    procedure.setDateUpdated(LocalDate.now());
                    procedure.setDateAdded(LocalDate.now());
                    procedure.setClinic(clinic);
                    procedureRepository.save(procedure);
                } else {
                    updateProcedure(existingProcedure, procedure);
                    procedureRepository.save(existingProcedure);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error processing the Excel file", e);
        }
    }

    @Transactional
    public void exportProcedures(String clinicId, HttpServletResponse response, ExcelExportRequest excelExportRequest) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = determineFileNameAndCreateTable(excelExportRequest, "procedure", clinicId);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s%s.xlsx\"", fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    @Transactional
    public void exportExcelProceduresTemplate(HttpServletResponse response) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = "procedures_template_import_";
        writeHeaderLine("Template", "procedures");
        adjustColumnWidths();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s%s.xlsx\"", fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    private boolean isValidHeader(Row headerRow) {
        List<String> expectedHeaders = List.of(
                ID_EXPORT_NAME, NAME_EXPORT_NAME, DESCRIPTION_EXPORT_NAME, TAX_RATE_PERCENT_EXPORT_NAME,
                FINAL_PRICE_EXPORT_NAME, DATE_ADDED_EXPORT_NAME, DATE_UPDATED_EXPORT_NAME
        );

        for (int i = 0; i < expectedHeaders.size(); i++) {
            if (!headerRow.getCell(i).getStringCellValue().equals(expectedHeaders.get(i))) {
                return false;
            }
        }
        return true;
    }

    private Procedure mapRowToProcedure(Row row) {
        String uuid = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null;
        for (int i = 1; i <= 4; i++) {
            if (row.getCell(i) == null) {
                throw new BadRequestException("Invalid Excel format: cell on Row; do not match expected format.");
            }
        }

        BigDecimal finalPrice = BigDecimal.valueOf(row.getCell(4).getNumericCellValue());
        BigDecimal taxRatePercent = BigDecimal.valueOf(row.getCell(3).getNumericCellValue());
        BigDecimal taxRateMultiplier = BigDecimal.ONE.add(taxRatePercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        BigDecimal priceBeforeTaxes = finalPrice.divide(taxRateMultiplier, 2, RoundingMode.HALF_UP);

        return Procedure.builder()
                .id(uuid != null ? UUID.fromString(uuid) : UUID.randomUUID())
                .name(row.getCell(1).getStringCellValue())
                .description(row.getCell(2).getStringCellValue())
                .price(priceBeforeTaxes)
                .taxRatePercent(taxRatePercent)
                .finalPrice(finalPrice)
                .isActive(true)
                .build();
    }

    private void updateProcedure(Procedure existingProcedure, Procedure newProcedure) {
        updateProcedureField(newProcedure::getName, existingProcedure::getName, existingProcedure::setName);
        updateProcedureField(newProcedure::getDescription, existingProcedure::getDescription, existingProcedure::setDescription);
        if (!newProcedure.getPrice().equals(existingProcedure.getPrice())) {
            existingProcedure.setPrice(newProcedure.getPrice());
        }
        if (!newProcedure.getFinalPrice().equals(existingProcedure.getFinalPrice())) {
            existingProcedure.setFinalPrice(newProcedure.getFinalPrice());
        }
        if (!newProcedure.getTaxRatePercent().equals(existingProcedure.getTaxRatePercent())) {
            existingProcedure.setTaxRatePercent(newProcedure.getTaxRatePercent());
        }
        existingProcedure.setDateUpdated(LocalDate.now());
    }

    private void updateProcedureField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private String determineFileNameAndCreateTable(ExcelExportRequest excelExportRequest, String exportedObjectName, String clinicId) {
        ExportTypeEnum exportType = ExportTypeEnum.fromValue(excelExportRequest.exportType());
        String exportTypeString = exportType.getExportType();
        return switch (exportType) {
            case ExportTypeEnum.ALL -> {
                createTable(procedureRepository.findAllByClinic_Id(UUID.fromString(clinicId)), exportTypeString, exportedObjectName);
                yield "all_" + exportedObjectName + "_";
            }
            case ExportTypeEnum.SELECTED -> {
                createTable(procedureRepository.findAllByIdIn(excelExportRequest.uuids()), exportTypeString, exportedObjectName);
                yield "selected_" + exportedObjectName + "_";
            }
        };
    }

    private void createTable(List<Procedure> procedures, String exportType, String exportedObjectName) {
        writeHeaderLine(exportType, exportedObjectName);
        writeDataLines(procedures);
        adjustColumnWidths();
    }

    private void writeHeaderLine(String exportType, String exportedObjectName) {
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        sheet = workbook.createSheet(String.format("%s %s", exportType, exportedObjectName));
        Row row = sheet.createRow(0);

        List<String> headers = getHeaders();
        for (int columnCount = 0; columnCount < headers.size(); columnCount++) {
            createCell(row, columnCount, headers.get(columnCount), headerCellStyle);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(String.valueOf(value));
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int columnCount, Object value) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Number number) {
            cell.setCellValue((number).doubleValue());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    private List<String> getHeaders() {
        return List.of(ID_EXPORT_NAME, NAME_EXPORT_NAME, DESCRIPTION_EXPORT_NAME, TAX_RATE_PERCENT_EXPORT_NAME,
                FINAL_PRICE_EXPORT_NAME, DATE_ADDED_EXPORT_NAME, DATE_UPDATED_EXPORT_NAME);
    }

    private void writeDataLines(List<Procedure> procedures) {
        int rowCount = 1;
        for (Procedure procedure : procedures) {
            Row row = sheet.createRow(rowCount++);
            fillRowWithData(row, procedure);
        }
    }

    private void fillRowWithData(Row row, Procedure procedure) {
        int columnCount = 0;

        createCell(row, columnCount++, procedure.getId());
        createCell(row, columnCount++, procedure.getName());
        createCell(row, columnCount++, procedure.getDescription());
        createCell(row, columnCount++, procedure.getTaxRatePercent());
        createCell(row, columnCount++, procedure.getFinalPrice());
        createCell(row, columnCount++, procedure.getDateAdded().toString());
        createCell(row, columnCount, procedure.getDateUpdated().toString());
    }

    private void adjustColumnWidths() {
        int numberOfColumns = getHeaders().size();
        for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            int maxWidth = 0;
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        int cellLength = getCellContentLength(cell);
                        if (cellLength > maxWidth) {
                            maxWidth = cellLength;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnIndex, Math.min(maxWidth * 256, 255 * 256));
        }
    }

    private int getCellContentLength(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().length();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue()).length();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue()).length();
            case FORMULA -> cell.getCellFormula().length();
            default -> 0;
        };
    }
}
