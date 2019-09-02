package javagda25;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        EntityDao entityDao = new EntityDao();
        InvoiceDao invoiceDao = new InvoiceDao();
        Long idL;

        Scanner sc = new Scanner(System.in);

        System.out.println("Program do operacjach na danych w bazie");
        System.out.println("Invoice");

        boolean quit = false;

        String menuItem;

        do {
            System.out.println("Możliwe opcje:");
            System.out.println("-add-  -takeInvoice-  -addproduct-  -pay-  -allUnpaid-  -list-  -getId-  -listProd-  -edit  -quit-");
            int id;
            menuItem = sc.nextLine().toLowerCase();

            switch (menuItem) {

                case "add":

                    System.out.println("Add invoice:");
                    Invoice invoice = new Invoice();
                    System.out.println("Client name:");
                    invoice.setNazwaKlijenta(sc.nextLine());
                    System.out.println("Invoice :");
                    invoice.setCzyOplacony(false);
                    /*
                    invoice.se(Integer.parseInt(sc.nextLine()));
                    //System.out.println("Student average:");
                    //student.setAverage(Double.parseDouble(sc.nextLine()));
                    System.out.println("Student alive?:");
                    student.setAlive(Boolean.parseBoolean(sc.nextLine()));
*/
                    entityDao.saveOrUpdate(invoice);


                    break;

                case "addproduct":

                    System.out.println("Add product:");

                    InvoicePosition pos = new InvoicePosition();
                    System.out.println("Client id:");
                    Optional<Invoice> invoiceOptional = entityDao.getById(Invoice.class,  Long.parseLong(sc.nextLine()));
                    Invoice invoice1 = invoiceOptional.get();
                    if(invoice1.getCzyOplacony() ){
                        System.out.println("Invoice is paid. You can't add new product");
                        break;
                    }
                    pos.setInvoice(invoice1);
                    System.out.println("Product:");
                    pos.setNazwa(sc.nextLine());
                    System.out.println("Proce:");
                    pos.setCena(Double.parseDouble(sc.nextLine()));
                    pos.setPodatek(pos.getCena()*0.23);
                    System.out.println("Quantity:");
                    pos.setIlosc(Integer.parseInt(sc.nextLine()));
                    entityDao.saveOrUpdate(pos);

                    break;

                case "takeinvoice":
                    System.out.println("Take invoice:");
                    System.out.println("Invoice id?:");
                     idL = Long.parseLong(sc.nextLine());
                    Invoice invoice3 = entityDao.getById(Invoice.class, idL).get();
                    invoice3.setDataWydania(LocalDateTime.now());
                    System.out.println("Client name :" + invoice3.getNazwaKlijenta());
                    System.out.println("Invoice sum :" + invoice3.getBillValue());
                    System.out.println("Invoice relese data :" + invoice3.getDataWydania());
                    System.out.println("Invoice open data :" + invoice3.getDataDodania());
                    System.out.println("Products:");
                    entityDao.getById(Invoice.class, idL).get().getProductList().forEach(System.out::println);
                    entityDao.saveOrUpdate(invoice3);

                    break;

                case "pay":
                    System.out.println("Pay invoice:");
                    System.out.println("Invoice id?:");
                     idL = Long.parseLong(sc.nextLine());
                    Invoice invoice2 = entityDao.getById(Invoice.class, idL).get();
                    invoice2.setCzyOplacony(true);

                    invoice2.setDataIGodzinaOpłacenia(LocalDateTime.now());
                    entityDao.saveOrUpdate(invoice2);

                    break;

                case "allunpaid":

                    System.out.println("All unpaid invoids:");

                    invoiceDao.getAllUnpaid().forEach(System.out::println);




                    break;

                case "list":
                    System.out.println("List invoices");


                    entityDao.getAll(Invoice.class).forEach(System.out::println);
                    //studentDao.listStudents().forEach(System.out::println);


                    break;

                case "getid":

                    System.out.println("Get one client by id.");

                    System.out.println("Client id?:");
                    idL = Long.parseLong(sc.nextLine());
                    System.out.println(entityDao.getById(Invoice.class, idL).toString());

                    break;

                case "listprod":

                    System.out.println("Invoice id:");
                    idL = Long.parseLong(sc.nextLine());
                    System.out.println(entityDao.getById(Invoice.class, idL).get().getProductList());



                    break;

                case "edit":

                    System.out.println("Edit student");

                    break;

                case "quit":

                    System.out.println("You've chosen qiut ");

                    quit = true;
                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (!quit);

        System.out.println("Bye-bye!");


    }
}
