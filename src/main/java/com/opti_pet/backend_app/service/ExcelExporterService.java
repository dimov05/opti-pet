package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.persistence.enums.ExportTypeEnum;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Consumable;
import com.opti_pet.backend_app.persistence.model.Medication;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.persistence.repository.ConsumableRepository;
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
    private final ConsumableRepository consumableRepository;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final ProcedureRepository procedureRepository;

    @Transactional
    public void importProcedures(String clinicId, MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook1 = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet1 = workbook1.getSheetAt(0);

            Row headerRow = sheet1.getRow(0);
            if (isHeaderInvalid(headerRow, PROCEDURE_EXPORT_TYPE)) {
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
        String fileName = String.format(FILE_NAME_TEMPLATE_IMPORT, PROCEDURE_EXPORT_TYPE);
        List<String> headers = getHeadersByObject(PROCEDURE_EXPORT_TYPE);
        writeHeaderLine(TEMPLATE, PROCEDURE_EXPORT_TYPE, headers);
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
            if (isHeaderInvalid(headerRow, MEDICATION_EXPORT_TYPE)) {
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
        String fileName = String.format(FILE_NAME_TEMPLATE_IMPORT, MEDICATION_EXPORT_TYPE);
        List<String> headers = getHeadersByObject(MEDICATION_EXPORT_TYPE);
        writeHeaderLine(TEMPLATE, MEDICATION_EXPORT_TYPE, headers);
        adjustColumnWidths(getMedicationHeaders().size());
        response.setContentType(EXPORT_EXCEL_CONTENT_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT_FILE_NAME, fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    @Transactional
    public void importConsumables(String clinicId, MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook1 = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet1 = workbook1.getSheetAt(0);

            Row headerRow = sheet1.getRow(0);
            if (isHeaderInvalid(headerRow, CONSUMABLE_EXPORT_TYPE)) {
                throw new BadRequestException(INVALID_EXCEL_FORMAT_HEADERS_DO_NOT_MATCH_TEMPLATE_EXCEPTION);
            }

            Iterator<Row> rowIterator = sheet1.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Consumable consumable = mapRowToConsumable(row);

                Consumable existingConsumable = consumableRepository.findById(consumable.getId()).orElse(null);
                if (existingConsumable == null) {
                    Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
                    consumable.setDateUpdated(LocalDate.now());
                    consumable.setDateAdded(LocalDate.now());
                    consumable.setClinic(clinic);
                    consumableRepository.save(consumable);
                } else {
                    updateConsumable(existingConsumable, consumable);
                    consumableRepository.save(existingConsumable);
                }
            }
        } catch (IOException e) {
            throw new IOException(ERROR_PROCESSING_EXCEL_FILE_TEMPLATE_EXCEPTION, e);
        }
    }

    @Transactional
    public void exportConsumables(String clinicId, HttpServletResponse response, ExcelExportRequest excelExportRequest) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = determineFileNameAndCreateTable(excelExportRequest, CONSUMABLE_EXPORT_TYPE, clinicId);

        response.setContentType(EXPORT_EXCEL_CONTENT_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT_FILE_NAME, fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    @Transactional
    public void exportExcelConsumablesTemplate(HttpServletResponse response) throws IOException {
        workbook = new XSSFWorkbook();
        String fileName = String.format(FILE_NAME_TEMPLATE_IMPORT, CONSUMABLE_EXPORT_TYPE);
        List<String> headers = getHeadersByObject(CONSUMABLE_EXPORT_TYPE);
        writeHeaderLine(TEMPLATE, CONSUMABLE_EXPORT_TYPE, headers);
        adjustColumnWidths(getConsumableHeaders().size());
        response.setContentType(EXPORT_EXCEL_CONTENT_TYPE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT_FILE_NAME, fileName, LocalDate.now()));

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    private boolean isHeaderInvalid(Row headerRow, String exportedObjectName) {
        List<String> expectedHeaders = switch (exportedObjectName) {
            case PROCEDURE_EXPORT_TYPE -> getProcedureHeaders();
            case MEDICATION_EXPORT_TYPE -> getMedicationHeaders();
            case CONSUMABLE_EXPORT_TYPE -> getConsumableHeaders();
            default -> throw new BadRequestException(UNEXPECTED_VALUE_EXCEPTION + exportedObjectName);
        };

        for (int i = 0; i < expectedHeaders.size(); i++) {
            if (!headerRow.getCell(i).getStringCellValue().equals(expectedHeaders.get(i))) {
                return true;
            }
        }
        return false;
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

    private Consumable mapRowToConsumable(Row row) {
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

        return Consumable.builder()
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

    private void updateConsumable(Consumable existingConsumable, Consumable newConsumable) {
        updateField(newConsumable::getName, existingConsumable::getName, existingConsumable::setName);
        updateField(newConsumable::getDescription, existingConsumable::getDescription, existingConsumable::setDescription);
        if (!newConsumable.getPrice().equals(existingConsumable.getPrice())) {
            existingConsumable.setPrice(newConsumable.getPrice());
        }
        if (!newConsumable.getFinalPrice().equals(existingConsumable.getFinalPrice())) {
            existingConsumable.setFinalPrice(newConsumable.getFinalPrice());
        }
        if (!newConsumable.getTaxRatePercent().equals(existingConsumable.getTaxRatePercent())) {
            existingConsumable.setTaxRatePercent(newConsumable.getTaxRatePercent());
        }
        if (!newConsumable.getAvailableQuantity().equals(existingConsumable.getAvailableQuantity())) {
            existingConsumable.setAvailableQuantity(newConsumable.getAvailableQuantity());
        }
        existingConsumable.setDateUpdated(LocalDate.now());
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
                switch (exportedObjectName) {
                    case PROCEDURE_EXPORT_TYPE -> {
                        createProceduresTable(procedureRepository.findAllByClinic_Id(UUID.fromString(clinicId)), exportTypeString, exportedObjectName);
                        yield "all_" + exportedObjectName + "_";
                    }
                    case MEDICATION_EXPORT_TYPE -> {
                        createMedicationsTable(medicationRepository.findAllByClinic_Id(UUID.fromString(clinicId)), exportTypeString, exportedObjectName);
                        yield "all_" + exportedObjectName + "_";
                    }
                    case CONSUMABLE_EXPORT_TYPE -> {
                        createConsumablesTable(consumableRepository.findAllByClinic_Id(UUID.fromString(clinicId)), exportTypeString, exportedObjectName);
                        yield "all_" + exportedObjectName + "_";
                    }
                    default -> throw new BadRequestException(UNEXPECTED_VALUE_EXCEPTION + exportedObjectName);

                }
            }
            case ExportTypeEnum.SELECTED -> {
                switch (exportedObjectName) {
                    case PROCEDURE_EXPORT_TYPE -> {
                        createProceduresTable(procedureRepository.findAllByIdIn(excelExportRequest.uuids()), exportTypeString, exportedObjectName);
                        yield "selected_" + exportedObjectName + "_";
                    }
                    case MEDICATION_EXPORT_TYPE -> {
                        createMedicationsTable(medicationRepository.findAllByIdIn(excelExportRequest.uuids()), exportTypeString, exportedObjectName);
                        yield "all_" + exportedObjectName + "_";
                    }
                    case CONSUMABLE_EXPORT_TYPE -> {
                        createConsumablesTable(consumableRepository.findAllByIdIn(excelExportRequest.uuids()), exportTypeString, exportedObjectName);
                        yield "all_" + exportedObjectName + "_";
                    }
                    default -> throw new BadRequestException(UNEXPECTED_VALUE_EXCEPTION + exportedObjectName);

                }

            }
        };
    }

    private void createProceduresTable(List<Procedure> procedures, String exportType, String exportedObjectName) {
        List<String> headers = getHeadersByObject(exportedObjectName);
        writeHeaderLine(exportType, exportedObjectName, headers);
        writeProceduresDataLines(procedures);
        adjustColumnWidths(headers.size());
    }

    private void createMedicationsTable(List<Medication> medications, String exportType, String exportedObjectName) {
        List<String> headers = getHeadersByObject(exportedObjectName);
        writeHeaderLine(exportType, exportedObjectName, headers);
        writeMedicationsDataLines(medications);
        adjustColumnWidths(headers.size());
    }

    private void createConsumablesTable(List<Consumable> consumables, String exportType, String exportedObjectName) {
        List<String> headers = getHeadersByObject(exportedObjectName);
        writeHeaderLine(exportType, exportedObjectName, headers);
        writeConsumablesDataLines(consumables);
        adjustColumnWidths(headers.size());
    }

    private List<String> getHeadersByObject(String exportedObjectName) {
        return switch (exportedObjectName) {
            case PROCEDURE_EXPORT_TYPE -> getProcedureHeaders();
            case MEDICATION_EXPORT_TYPE -> getMedicationHeaders();
            case CONSUMABLE_EXPORT_TYPE -> getConsumableHeaders();
            default -> throw new BadRequestException(UNEXPECTED_VALUE_EXCEPTION + exportedObjectName);
        };
    }


    private void writeHeaderLine(String exportType, String exportedObjectName, List<String> headers) {
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        sheet = workbook.createSheet(String.format("%s %s", exportType, exportedObjectName));
        Row row = sheet.createRow(0);
        CellStyle lockedHeaderCellStyle = createCellStyle(headerFont, IndexedColors.DARK_RED);
        CellStyle editableHeaderCellStyle = createCellStyle(headerFont, IndexedColors.LIGHT_YELLOW);

        for (int columnCount = 0; columnCount < headers.size(); columnCount++) {
            boolean isLocked = headers.get(columnCount).equals(ID_EXPORT_NAME) ||
                    headers.get(columnCount).equals(DATE_ADDED_EXPORT_NAME) ||
                    headers.get(columnCount).equals(DATE_UPDATED_EXPORT_NAME);
            createCell(row, columnCount, headers.get(columnCount), isLocked ? lockedHeaderCellStyle : editableHeaderCellStyle);
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

    private CellStyle createCellStyle(XSSFFont font, IndexedColors bgColor) {
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(bgColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true); // Optional: wraps text within the cell

        // You may also want to set borders for better visual appearance
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
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

    private List<String> getConsumableHeaders() {
        return List.of(ID_EXPORT_NAME, NAME_EXPORT_NAME, DESCRIPTION_EXPORT_NAME, AVAILABLE_QUANTITY_EXPORT_NAME,
                TAX_RATE_PERCENT_EXPORT_NAME, FINAL_PRICE_EXPORT_NAME, DATE_ADDED_EXPORT_NAME, DATE_UPDATED_EXPORT_NAME);
    }

    private void writeProceduresDataLines(List<Procedure> procedures) {
        int rowCount = 1;
        for (Procedure procedure : procedures) {
            Row row = sheet.createRow(rowCount++);
            fillRowWithProcedureData(row, procedure);
        }
    }

    private void writeMedicationsDataLines(List<Medication> medications) {
        int rowCount = 1;
        for (Medication medication : medications) {
            Row row = sheet.createRow(rowCount++);
            fillRowWithMedicationData(row, medication);
        }
    }

    private void writeConsumablesDataLines(List<Consumable> consumables) {
        int rowCount = 1;
        for (Consumable consumable : consumables) {
            Row row = sheet.createRow(rowCount++);
            fillRowWithConsumableData(row, consumable);
        }
    }

    private void fillRowWithProcedureData(Row row, Procedure procedure) {
        int columnCount = 0;
        createCell(row, columnCount++, procedure.getId());
        createCell(row, columnCount++, procedure.getName());
        createCell(row, columnCount++, procedure.getDescription());
        createCell(row, columnCount++, procedure.getTaxRatePercent());
        createCell(row, columnCount++, procedure.getFinalPrice());
        createCell(row, columnCount++, procedure.getDateAdded().toString());
        createCell(row, columnCount, procedure.getDateUpdated().toString());
    }

    private void fillRowWithMedicationData(Row row, Medication medication) {
        int columnCount = 0;
        createCell(row, columnCount++, medication.getId());
        createCell(row, columnCount++, medication.getName());
        createCell(row, columnCount++, medication.getDescription());
        createCell(row, columnCount++, medication.getAvailableQuantity());
        createCell(row, columnCount++, medication.getTaxRatePercent());
        createCell(row, columnCount++, medication.getFinalPrice());
        createCell(row, columnCount++, medication.getDateAdded().toString());
        createCell(row, columnCount, medication.getDateUpdated().toString());
    }

    private void fillRowWithConsumableData(Row row, Consumable consumable) {
        int columnCount = 0;
        createCell(row, columnCount++, consumable.getId());
        createCell(row, columnCount++, consumable.getName());
        createCell(row, columnCount++, consumable.getDescription());
        createCell(row, columnCount++, consumable.getAvailableQuantity());
        createCell(row, columnCount++, consumable.getTaxRatePercent());
        createCell(row, columnCount++, consumable.getFinalPrice());
        createCell(row, columnCount++, consumable.getDateAdded().toString());
        createCell(row, columnCount, consumable.getDateUpdated().toString());
    }
}
