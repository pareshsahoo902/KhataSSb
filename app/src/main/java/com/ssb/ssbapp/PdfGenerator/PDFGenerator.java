package com.ssb.ssbapp.PdfGenerator;

import android.Manifest;
import android.content.Context;
import android.graphics.fonts.Font;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ssb.ssbapp.Customer.CustomerModel;
import com.ssb.ssbapp.Model.TrayMasterModel;
import com.ssb.ssbapp.TransactionModel.MoneyTransactionModel;
import com.ssb.ssbapp.TransactionPage.MoneyTransaction;
import com.ssb.ssbapp.TrayModels.TrayModelItem;
import com.ssb.ssbapp.TrayModels.TrayTransactionModel;
import com.ssb.ssbapp.Utils.UtilsMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PDFGenerator {

    private Context mContext;
    private double totalGave, totalGot, netBalance, openingBalance;

    private ArrayList<MoneyTransactionModel> modelArrayList;
    private ArrayList<TrayTransactionModel> trayModelList;

    public PDFGenerator(Context mContext) {
        this.mContext = mContext;
        Dexter.withContext(mContext)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                });
    }


    ////////////////////////////////////MONEY/////////////////////////////////////////////////////////////

    public boolean createMoneyReport(String filename, ArrayList<MoneyTransactionModel> itemList, CustomerModel model,String dateRange, boolean isParty) {

        modelArrayList = new ArrayList<>();
        modelArrayList = itemList;

        String customerName;
        if (model == null) {
            customerName = "SSB";
        } else {
            customerName = model.getName();
        }

        calculateValues(itemList);

        File ssb = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SSB");
        if (!ssb.mkdirs()) {
            ssb.mkdirs();
        }

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "SSB/" + filename + ".pdf");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);

            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);


            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph("SSB");
            paragraph.setTextAlignment(TextAlignment.CENTER);
            paragraph.setFontColor(ColorConstants.BLUE);
            paragraph.setFontSize(28);
            paragraph.setBold();
            document.add(paragraph);


            Paragraph titlePara = new Paragraph(customerName + "'s Statement");
            titlePara.setTextAlignment(TextAlignment.CENTER);
            titlePara.setFontSize(15);
            titlePara.setBold();
            document.add(titlePara);

            document.add(new Paragraph("("+dateRange+")").setFontColor(ColorConstants.BLUE).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));


            float[] widths = {200, 200, 200, 200};
            Table table1 = new Table(widths);
            table1.setTextAlignment(TextAlignment.CENTER);

            table1.addCell(new Cell().add(new Paragraph("Opening Balance").setFontSize(14).setTextAlignment(TextAlignment.CENTER)));
            table1.addCell(new Cell().add(new Paragraph("Total Got").setFontSize(14).setTextAlignment(TextAlignment.CENTER)));
            table1.addCell(new Cell().add(new Paragraph("Total Gave").setFontSize(14).setTextAlignment(TextAlignment.CENTER)));
            table1.addCell(new Cell().add(new Paragraph("Net Balance").setFontSize(14).setTextAlignment(TextAlignment.CENTER)));


            table1.addCell(new Cell(2, 1).add(new Paragraph("Rs " + openingBalance).setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER)));
            table1.addCell(new Cell(2, 1).add(new Paragraph("Rs " + totalGot).setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER)));
            table1.addCell(new Cell(2, 1).add(new Paragraph("Rs " + totalGave).setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER)));
            table1.addCell(new Cell(2, 1).add(new Paragraph("Rs " + netBalance).setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER)));

            document.add(table1);


            float[] widthColoumn = {150, 400, 200, 200, 200};
            Table table2 = new Table(widthColoumn);
            table2.addCell(new Cell().add(new Paragraph("Date").setBold().setFontSize(14).setBackgroundColor(ColorConstants.LIGHT_GRAY)));
            table2.addCell(new Cell().add(new Paragraph("Details").setBold().setFontSize(14).setBackgroundColor(ColorConstants.LIGHT_GRAY)));
            table2.addCell(new Cell().add(new Paragraph("Got(+)").setTextAlignment(TextAlignment.RIGHT).setBold().setFontSize(14).setBackgroundColor(ColorConstants.LIGHT_GRAY)));
            table2.addCell(new Cell().add(new Paragraph("Gave(-)").setTextAlignment(TextAlignment.RIGHT).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(14)));
            table2.addCell(new Cell().add(new Paragraph("Balance").setTextAlignment(TextAlignment.RIGHT).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(14)));

            for (int i = 0; i < modelArrayList.size(); i++) {

                table2.addCell(new Cell().add(new Paragraph(modelArrayList.get(i).getDate().substring(0, 10)).setTextAlignment(TextAlignment.LEFT).setFontSize(12)));
                table2.addCell(new Cell().add(new Paragraph(modelArrayList.get(i).getDescription()).setFontSize(12)));
                if (modelArrayList.get(i).getStatus().equals("got")) {
                    table2.addCell(new Cell().add(new Paragraph("Rs" + String.valueOf(modelArrayList.get(i).getTotal())).setTextAlignment(TextAlignment.RIGHT).setFontSize(14)));
                    table2.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(14)));

                } else {
                    table2.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(14)));
                    table2.addCell(new Cell().add(new Paragraph("Rs" + String.valueOf(modelArrayList.get(i).getTotal())).setTextAlignment(TextAlignment.RIGHT).setFontSize(14)));
                }

                table2.addCell(new Cell().add(new Paragraph("Rs" + String.valueOf(calcBalance(i))).setTextAlignment(TextAlignment.RIGHT).setFontSize(16)));
            }


            document.add(new Paragraph("\n"));
            document.add(table2);


            float[] column = {550, 200, 200, 200};
            Table grandTotal = new Table(column);

            grandTotal.addCell(new Cell().add(new Paragraph("Grand Total").setFontSize(18)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            grandTotal.addCell(new Cell().add(new Paragraph("(+)Rs " + totalGot).setTextAlignment(TextAlignment.RIGHT).setFontSize(16)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            grandTotal.addCell(new Cell().add(new Paragraph("(-)Rs " + totalGave).setTextAlignment(TextAlignment.RIGHT).setFontSize(16)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            grandTotal.addCell(new Cell().add(new Paragraph("Rs " + netBalance).setTextAlignment(TextAlignment.RIGHT).setFontColor(ColorConstants.BLUE).setFontSize(18)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            document.add(new Paragraph("\n"));
            document.add(grandTotal);

            if (isParty) {

                ArrayList<MoneyTransactionModel> modelList = getChalanData(itemList);

                for (int i = 0; i < modelList.size(); i++) {


                    document.add(new AreaBreak());
                    Paragraph chalan = new Paragraph("E-CHALAN RECEIPT " + String.valueOf(i + 1));
                    chalan.setTextAlignment(TextAlignment.CENTER);
                    chalan.setFontColor(ColorConstants.BLUE);
                    chalan.setFontSize(28);
                    chalan.setBold();
                    document.add(new Paragraph("\n"));

                    document.add(chalan);
                    document.add(new Paragraph("\n"));

                    document.add(new Paragraph("\n"));
                    document.add(new Paragraph("Date: " + modelList.get(i).getDate() + "\tName: " + customerName).setFontSize(20));
                    document.add(new Paragraph("\n"));

                    document.add(new Paragraph("Details: " + modelList.get(i).getDescription()).setFontSize(20));
                    document.add(new Paragraph("Particular: " + removeMPlus(modelList.get(i).getDetails()))
                            .setMarginRight(10).setTextAlignment(TextAlignment.RIGHT).setFontSize(20));


                    float[] chalanColoum = {250, 200, 200, 200, 300};
                    Table chalanT = new Table(chalanColoum);
                    chalanT.addCell(new Cell().add(new Paragraph("Commision").setBold().setFontSize(14).setBackgroundColor(ColorConstants.LIGHT_GRAY)));
                    chalanT.addCell(new Cell().add(new Paragraph("Labour").setBold().setFontSize(14).setBackgroundColor(ColorConstants.LIGHT_GRAY)));
                    chalanT.addCell(new Cell().add(new Paragraph("fair").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(14)));
                    chalanT.addCell(new Cell().add(new Paragraph("Extra").setBold().setFontSize(14).setBackgroundColor(ColorConstants.LIGHT_GRAY)));
                    chalanT.addCell(new Cell().add(new Paragraph("Total Less").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(14)));


                    chalanT.addCell(new Cell(2, 1).add(new Paragraph("(" + modelList.get(i).getPartyModel().getCommPercent() + ")"
                            + String.valueOf(modelList.get(i).getPartyModel().getCommision())).setBold().setFontSize(18)));
                    chalanT.addCell(new Cell(2, 1).add(new Paragraph(String.valueOf(modelList.get(i).getPartyModel().getLabour())).setBold().setFontSize(18)));
                    chalanT.addCell(new Cell(2, 1).add(new Paragraph(String.valueOf(modelList.get(i).getPartyModel().getFair())).setBold().setFontSize(18)));
                    chalanT.addCell(new Cell(2, 1).add(new Paragraph(String.valueOf(modelList.get(i).getPartyModel().getExtra())).setBold().setFontSize(18)));
                    chalanT.addCell(new Cell(2, 1).add(new Paragraph(String.valueOf(modelList.get(i).getPartyModel().getTotal())).setBold().setFontSize(18)));


                    document.add(chalanT);

                    float[] chawidht = {600, 600};
                    Table chalTotalTabel = new Table(chawidht);


                    chalTotalTabel.addCell(new Cell(2, 1).add(new Paragraph("Grand Total").setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    chalTotalTabel.addCell(new Cell(2, 1).add(new Paragraph(String.valueOf(modelList.get(i).getTotal())).setBold().setFontSize(20).setTextAlignment(TextAlignment.RIGHT)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                    document.add(new Paragraph("\n"));
                    document.add(chalTotalTabel);

                }
            }

            document.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("paresh", e.toString());
            return false;
        }
    }


    private ArrayList<MoneyTransactionModel> getChalanData(ArrayList<MoneyTransactionModel> itemList) {
        for (MoneyTransactionModel model : itemList) {
            if (!model.isParty()) {
                itemList.remove(model);
            }
        }
        return itemList;
    }
    ////////////////////////////////////TRAY/////////////////////////////////////////////////////////////

    public boolean createTrayDuePdf(String filename, ArrayList<TrayTransactionModel> itemList, ArrayList<TrayMasterModel> trayList, CustomerModel model, String dateRange){

        trayModelList = new ArrayList<>();
        trayModelList = itemList;


        File ssb = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SSB");
        if (!ssb.mkdirs()) {
            ssb.mkdirs();
        }

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "SSB/" + filename + ".pdf");
        OutputStream outputStream = null;


        try {
            outputStream = new FileOutputStream(file);
            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            Document document = new Document(pdfDocument);

            Paragraph paragraph = new Paragraph("SSB");
            paragraph.setTextAlignment(TextAlignment.CENTER);
            paragraph.setFontColor(ColorConstants.BLUE);
            paragraph.setFontSize(28);
            paragraph.setBold();
            document.add(paragraph);


            Paragraph titlePara = new Paragraph(UtilsMethod.capitalize(model.getName()) + "'s Tray Dues");
            titlePara.setTextAlignment(TextAlignment.CENTER);
            titlePara.setFontSize(20);
            titlePara.setBold();
            document.add(titlePara);

            Paragraph dateCurrent = new Paragraph("Date: "+ UtilsMethod.getCurrentDate().substring(0,10));
            dateCurrent.setTextAlignment(TextAlignment.LEFT);
            dateCurrent.setFontSize(15);
            document.add(dateCurrent);

            float[] widths = {400, 300};
            Table table1 = new Table(widths);
            table1.setTextAlignment(TextAlignment.CENTER);

            table1.addCell(new Cell().add(new Paragraph("Tray Name").setFontSize(16).setBackgroundColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER)));
            table1.addCell(new Cell().add(new Paragraph("Due").setFontSize(16).setBackgroundColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER)));

            int totalCount=0;
            for (int i =0 ; i<trayList.size();i++){
                int totalDue = getTrayDue(trayList.get(i).getTid());

                if (totalDue>0){
                    totalCount +=totalDue;
                    table1.addCell(new Cell().add(new Paragraph(UtilsMethod.capitalize(trayList.get(i).getName())).setFontSize(16).setTextAlignment(TextAlignment.LEFT)));
                    table1.addCell(new Cell().add(new Paragraph(String.valueOf(totalDue)).setFontSize(16).setTextAlignment(TextAlignment.CENTER)));
                }
            }


            Paragraph totalPara = new Paragraph("Total Due: "+ String.valueOf(totalCount));
            totalPara.setTextAlignment(TextAlignment.LEFT);
            totalPara.setFontSize(19);
            totalPara.setBold();

            document.add(new Paragraph("\n"));
            document.add(table1);
            document.add(new Paragraph("\n"));
            document.add(totalPara);

            document.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("paresh", e.toString());
            return false;
        }

    }

    private int getTrayDue(String tid ) {
        int total =0;

        for (TrayTransactionModel model : trayModelList){
            if (model.getStatus().equals("gave")){

                for (TrayModelItem modelItem: model.getModelItemArrayList()){
                    if (modelItem.getId().equals(tid)){
                        total+=modelItem.getTotalCount();
                    }
                }

            }
        }


        return total;
    }


    private void calculateValues(ArrayList<MoneyTransactionModel> itemList) {
        totalGot = 0;
        totalGave = 0;

        for (MoneyTransactionModel model : itemList) {

            if (model.getStatus().equals("got")) {
                double t = Double.parseDouble((String) model.getTotal());
                totalGot += t;
            } else {
                double tg = Double.parseDouble((String) model.getTotal());
                totalGave += tg;
            }

        }

        netBalance = totalGave - totalGot;
        openingBalance = 0.0;
    }


    private double calcBalance(int position) {
        double balance = 0.0;

        if (position == 0) {
            balance = Double.parseDouble(modelArrayList.get(position).getTotal());
            return balance;
        } else {
            for (int i = 0; i <= position; i++) {
                if (modelArrayList.get(i).getStatus().equals("got")) {
                    balance += Double.parseDouble(modelArrayList.get(i).getTotal());
                } else {
                    balance -= Double.parseDouble(modelArrayList.get(i).getTotal());
                }
            }
        }
        return balance;
    }


    private String removeMPlus(String s) {
        String res = s;

        res = s.replaceAll("(M+)", "");

        return res;
    }


}
