package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.persistence.enums.ExportTypeEnum;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Medication;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.persistence.repository.MedicationRepository;
import com.opti_pet.backend_app.persistence.repository.ProcedureRepository;
import com.opti_pet.backend_app.rest.request.ExcelExportRequest;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
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
import static com.opti_pet.backend_app.util.ErrorConstants.*;

@Service
@RequiredArgsConstructor
public class ExcelExporterService {
    private final ClinicService clinicService;
    private final MedicationRepository medicationRepository;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final ProcedureRepository procedureRepository;

    @Transactional
    public void importProcedures(String clinicId, MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook1 = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet1 = workbook1.getSheetAt(0);

            Row headerRow = sheet1.getRow(0);
            if (!isValidHeader(headerRow, PROCEDURE_EXPORT_TYPE)) {
                throw new BadRequestException(INVALID_EXCEL_FORMAT_HEADERS_DO_NOT_MATCH_TEMPLATE_EXCEPTION);
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
            throw new IOException(ERROR_PROCESSING_EXCEL_FILE_TEMPLATE_EXCEPTION, e);
        }
    }

    @Transactional
    public void exportProcedures(String clinicId, HttpServletResponse response, ExcelExportRequest excelExportRequest) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = determineFileNameAndCreateTable(excelExportRequest, PROCEDURE_EXPORT_TYPE, clinicId);

        response.setContentType(EXPORT_EXCEL_CONTENT_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT_FILE_NAME, fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    @Transactional
    public void exportExcelProceduresTemplate(HttpServletResponse response) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = "procedures_template_import_";
        writeHeaderLine("Template", PROCEDURE_EXPORT_TYPE);
        adjustColumnWidths(getProcedureHeaders().size());
        response.setContentType(EXPORT_EXCEL_CONTENT_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT_FILE_NAME, fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    @Transactional
    public void importMedications(String clinicId, MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook1 = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet1 = workbook1.getSheetAt(0);

            Row headerRow = sheet1.getRow(0);
            if (!isValidHeader(headerRow, MEDICATION_EXPORT_TYPE)) {
                throw new BadRequestException(INVALID_EXCEL_FORMAT_HEADERS_DO_NOT_MATCH_TEMPLATE_EXCEPTION);
            }

            Iterator<Row> rowIterator = sheet1.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Medication medication = mapRowToMedication(row);

                Medication existingMedication = medicationRepository.findById(medication.getId()).orElse(null);
                if (existingMedication == null) {
                    Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
                    medication.setDateUpdated(LocalDate.now());
                    medication.setDateAdded(LocalDate.now());
                    medication.setClinic(clinic);
                    medicationRepository.save(medication);
                } else {
                    updateMedication(existingMedication, medication);
                    medicationRepository.save(existingMedication);
                }
            }
        } catch (IOException e) {
            throw new IOException(ERROR_PROCESSING_EXCEL_FILE_TEMPLATE_EXCEPTION, e);
        }
    }

    @Transactional
    public void exportMedications(String clinicId, HttpServletResponse response, ExcelExportRequest excelExportRequest) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = determineFileNameAndCreateTable(excelExportRequest, MEDICATION_EXPORT_TYPE, clinicId);

        response.setContentType(EXPORT_EXCEL_CONTENT_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT_FILE_NAME, fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    @Transactional
    public void exportExcelMedicationsTemplate(HttpServletResponse response) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = "medications_template_import_";
        writeHeaderLine("Template", MEDICATION_EXPORT_TYPE);
        adjustColumnWidths(getMedicationHeaders().size());
        response.setContentType(EXPORT_EXCEL_CONTENT_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT_FILE_NAME, fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    private boolean isValidHeader(Row headerRow, String exportedObjectName) {
        List<String> expectedHeaders = switch (exportedObjectName) {
            case PROCEDURE_EXPORT_TYPE -> getProcedureHeaders();
            case MEDICATION_EXPORT_TYPE -> getMedicationHeaders();
            default -> throw new BadRequestException(UNEXPECTED_VALUE_EXCEPTION + exportedObjectName);
        };

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
                throw new BadRequestException(String.format(INVALID_EXCEL_FORMAT_COLUMN_AND_ROW_TEMPLATE_EXCEPTION, i, row.getRowNum())
                );
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

    private Medication mapRowToMedication(Row row) {
        String uuid = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null;
        for (int i = 1; i <= 4; i++) {
            if (row.getCell(i) == null) {
                throw new BadRequestException(String.format(INVALID_EXCEL_FORMAT_COLUMN_AND_ROW_TEMPLATE_EXCEPTION, i, row.getRowNum()));
            }
        }

        BigDecimal finalPrice = BigDecimal.valueOf(row.getCell(5).getNumericCellValue());
        BigDecimal taxRatePercent = BigDecimal.valueOf(row.getCell(4).getNumericCellValue());
        BigDecimal taxRateMultiplier = BigDecimal.ONE.add(taxRatePercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        BigDecimal priceBeforeTaxes = finalPrice.divide(taxRateMultiplier, 2, RoundingMode.HALF_UP);

        return Medication.builder()
                .id(uuid != null ? UUID.fromString(uuid) : UUID.randomUUID())
                .name(row.getCell(1).getStringCellValue())
                .description(row.getCell(2).getStringCellValue())
                .price(priceBeforeTaxes)
                .availableQuantity(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()))
                .taxRatePercent(taxRatePercent)
                .finalPrice(finalPrice)
                .isActive(true)
                .build();
    }

    private void updateProcedure(Procedure existingProcedure, Procedure newProcedure) {
        updateField(newProcedure::getName, existingProcedure::getName, existingProcedure::setName);
        updateField(newProcedure::getDescription, existingProcedure::getDescription, existingProcedure::setDescription);
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

    private void updateMedication(Medication existingMedication, Medication newMedication) {
        updateField(newMedication::getName, existingMedication::getName, existingMedication::setName);
        updateField(newMedication::getDescription, existingMedication::getDescription, existingMedication::setDescription);
        if (!newMedication.getPrice().equals(existingMedication.getPrice())) {
            existingMedication.setPrice(newMedication.getPrice());
        }
        if (!newMedication.getFinalPrice().equals(existingMedication.getFinalPrice())) {
            existingMedication.setFinalPrice(newMedication.getFinalPrice());
        }
        if (!newMedication.getTaxRatePercent().equals(existingMedication.getTaxRatePercent())) {
            existingMedication.setTaxRatePercent(newMedication.getTaxRatePercent());
        }
        if (!newMedication.getAvailableQuantity().equals(existingMedication.getAvailableQuantity())) {
            existingMedication.setAvailableQuantity(newMedication.getAvailableQuantity());
        }
        existingMedication.setDateUpdated(LocalDate.now());
    }

    private void updateField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
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
        int headerSize = switch (exportedObjectName) {
            case PROCEDURE_EXPORT_TYPE -> getProcedureHeaders().size();
            case MEDICATION_EXPORT_TYPE -> getMedicationHeaders().size();
            default -> throw new BadRequestException(UNEXPECTED_VALUE_EXCEPTION + exportedObjectName);
        };
        writeHeaderLine(exportType, exportedObjectName);
        writeDataLines(procedures);
        adjustColumnWidths(headerSize);
    }

    private void writeHeaderLine(String exportType, String exportedObjectName) {
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setLocked(true);
        sheet = workbook.createSheet(String.format("%s %s", exportType, exportedObjectName));
        Row row = sheet.createRow(0);
        CellStyle lockedHeaderCellStyle = workbook.createCellStyle();
        lockedHeaderCellStyle.setFont(headerFont);
        lockedHeaderCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
        lockedHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        lockedHeaderCellStyle.setLocked(true);

        CellStyle editableHeaderCellStyle = workbook.createCellStyle();
        editableHeaderCellStyle.setFont(headerFont);
        editableHeaderCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        editableHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        editableHeaderCellStyle.setLocked(true);

        List<String> headers = switch (exportedObjectName) {
            case PROCEDURE_EXPORT_TYPE -> getProcedureHeaders();
            case MEDICATION_EXPORT_TYPE -> getMedicationHeaders();
            default -> throw new BadRequestException(UNEXPECTED_VALUE_EXCEPTION + exportedObjectName);
        };
        for (int columnCount = 0; columnCount < headers.size(); columnCount++) {
            if (headers.get(columnCount).equals(ID_EXPORT_NAME) ||
                    headers.get(columnCount).equals(DATE_ADDED_EXPORT_NAME) ||
                    headers.get(columnCount).equals(DATE_UPDATED_EXPORT_NAME)) {
                createCell(row, columnCount, headers.get(columnCount), lockedHeaderCellStyle);
            } else {
                createCell(row, columnCount, headers.get(columnCount), editableHeaderCellStyle);
            }
        }
    }

    private void adjustColumnWidths(int numberOfColumns) {
        for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
            int minWidth = 20 * 256;
            if (sheet.getColumnWidth(columnIndex) < minWidth) {
                sheet.setColumnWidth(columnIndex, minWidth);
            }
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        cell.setCellStyle(style);
        if (value instanceof Number number) {
            cell.setCellValue((number).doubleValue());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    private void createCell(Row row, int columnCount, Object value) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Number number) {
            cell.setCellValue((number).doubleValue());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    private List<String> getProcedureHeaders() {
        return List.of(ID_EXPORT_NAME, NAME_EXPORT_NAME, DESCRIPTION_EXPORT_NAME, TAX_RATE_PERCENT_EXPORT_NAME,
                FINAL_PRICE_EXPORT_NAME, DATE_ADDED_EXPORT_NAME, DATE_UPDATED_EXPORT_NAME);
    }

    private List<String> getMedicationHeaders() {
        return List.of(ID_EXPORT_NAME, NAME_EXPORT_NAME, DESCRIPTION_EXPORT_NAME, AVAILABLE_QUANTITY_EXPORT_NAME,
                TAX_RATE_PERCENT_EXPORT_NAME, FINAL_PRICE_EXPORT_NAME, DATE_ADDED_EXPORT_NAME, DATE_UPDATED_EXPORT_NAME);
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
}
