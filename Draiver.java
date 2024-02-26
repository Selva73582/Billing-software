import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class Writter {
    public static void writeDataToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Map<Integer, ArrayList<Object>> pro=Product.getProDetMap();
            for (Map.Entry<Integer, ArrayList<Object>> entry : pro.entrySet()) {
                ArrayList<Object> details = entry.getValue();
                String line =details.get(0) + "," + details.get(1) + "," + details.get(2)+ "," + details.get(3)  + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void SalesFile(String output) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SaleDetail.txt", true))) {
            writer.write(output);
            writer.newLine();
            System.out.println("Details appended to SaleDetail file");
        } catch (IOException e) {
            System.err.println("Error writing to SaleDetail file: " + e.getMessage());
            e.printStackTrace();  
        }
    }

    static void TotalSale(String output) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("TodaySales.txt"))) {
            writer.write(output);
            writer.newLine();
            System.out.println("Details appended to TodaySales file");
        } catch (IOException e) {
            System.err.println("Error writing to TodaySales file: " + e.getMessage());
            e.printStackTrace();  
        }
    }

    public static void loadProductFromCsv() {

        String filePath = "products.csv";  

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");  

        
                String name = fields[0];
                int qty = Integer.parseInt(fields[1]);
                int price = Integer.parseInt(fields[2]);
                int category = Integer.parseInt(fields[3]);

                new Product(name, qty, price, category);
                
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }
    }
}

class Product
{
    private int id;
    private String name;
    private int qty;
    private int price;
    private int category;
    static int count=0;
    private static Map<Integer,ArrayList<Object>> pro=new LinkedHashMap<>();
    Product(String name,int qty,int price,int category)
    {
        this.id=++count;
        this.name=name;
        this.qty=qty;
        this.price=price;
        this.category=category;
        ArrayList<Object> k=new ArrayList<>();
        k.add(this.name);
        k.add(this.qty);
        k.add(this.price);
        k.add(this.category);

        pro.put(this.id,k);
    }
    public static void getAllProudctDetails()
    {
        for(Map.Entry<Integer,ArrayList<Object>> m: pro.entrySet())
        {
            ArrayList<Object> details=m.getValue();
            System.out.print("Product name :" +details.get(0)+" Price: "+details.get(2) +" -->Id: "+m.getKey());
            System.out.println();
        }
    }
    public static Map<Integer,ArrayList<Object>> getProDetMap()
    {
        return pro;
    }

    public static ArrayList<Object> getProductDetails(int orderId)
    {
        return pro.get(orderId);
    }
    
    public static void removeProductById(int id2) {
        
        if(pro.containsKey(id2))
        {
            pro.remove(id2);
            System.out.println("--- product removed successfully due to Inefficiency----");
        }
        else
        {
            System.out.println("product not found !");
        }
    }
    public static void setProductPriceById(int id2) {
        Scanner sc=new Scanner(System.in);
        if(pro.containsKey(id2))
        {
            ArrayList<Object> list=pro.get(id2);
            System.out.println("Enter new price of product : ");
            int p=sc.nextInt();
            list.set(2,p);
            
        }
        else
        {
            System.out.println("product not found !");
        }
    }
    public static void setProductQuantityById(int id2,int qty) {
        Scanner sc=new Scanner(System.in);

        if(pro.containsKey(id2))
        {
            ArrayList<Object> list=pro.get(id2);
            int uqty=(int)list.get(1);
            int delpro=uqty-qty;
            if(delpro==0)
            {
                removeProductById(id2);
            }
            list.set(1,delpro);

            
        }
        else
        {
            System.out.println("product not found !");
        }
    }
    public static void checkQuantity() {
        for(Map.Entry<Integer,ArrayList<Object>> m: pro.entrySet())
        {
            ArrayList<Object> details=m.getValue();
            System.out.print(" -->Id: "+m.getKey()+ " Product name : " +details.get(0)+" Quantity : " + details.get(1));
            System.out.println();
        }
        Writter.writeDataToCSV("products.csv");
    }
    public static int getProductQuantity(int orderId) {
        if(pro.containsKey(orderId))
        {
            ArrayList<Object> prodet=pro.get(orderId);
            return (int)prodet.get(1);
        }

        return 0;
    }
    
    
}

class Sale
{
    private int saleId;
    private Product product;
    private int avilableqty;
    private String name;
    private float discount;
    private int category;
    private float afterDiscountPrice;
    private float discounted;
    private static int count=0;
    private static Map<Integer,ArrayList<Object>> saleDet=new LinkedHashMap<>();
    private static int daySale;
    static LocalDateTime myDateObj = LocalDateTime.now();
    static DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    static String formattedDate = myDateObj.format(myFormatObj);


    Sale(int orderId) {
        
        ArrayList<Object> proObjects = Product.getProductDetails(orderId);
        if(proObjects!=null)
        {
            Scanner sc=new Scanner(System.in);
            System.out.println("How much quantity you need : ");

            int pqty=sc.nextInt();
            int aqty=Product.getProductQuantity(orderId);
            System.out.println(aqty);
            while(pqty>aqty)
            {
                System.out.println("Available quantity only  "+ aqty );
                pqty=sc.nextInt();

            }
            this.saleId=++count;
            
            this.category=(int)proObjects.get(3);
            int p=(int)proObjects.get(2);
            if(this.category==1)
            {
                this.discount=0.3f;
            }
            else if(this.category==2)
            {
                this.discount=0.2f;
            }
            else if(this.category==3)
            {
                this.discount=0.1f;
            }
            else
            {
                this.discount=0.05f;
            }
            this.discounted=pqty*p*discount;
            this.afterDiscountPrice=(pqty*p)-(this.discounted);
            this.name=(String)proObjects.get(0);
            this.daySale +=this.afterDiscountPrice;
            ArrayList<Object> saleDeatils=new ArrayList<>();
            saleDeatils.add(this.name);
            saleDeatils.add(pqty);
            saleDeatils.add(this.discount);
            saleDeatils.add(this.discounted);
            saleDeatils.add(this.afterDiscountPrice);
            saleDeatils.add(formattedDate);
            saleDet.put(this.saleId,saleDeatils);
            System.out.println(saleDet);
            Product.setProductQuantityById(orderId,pqty);
        }
        else
        {
            System.out.println("Product not Found ! ");
        }
    }
    public static void completeSale()
    {
        for(Map.Entry<Integer,ArrayList<Object>> m: saleDet.entrySet())
        {
            ArrayList<Object> details=m.getValue();
            System.out.print(" --> Sale Id: "+m.getKey()+" Product name :" +details.get(0)+" Quantity :"+details.get(1));
            System.out.println();
        }
    }
    public static Map<Integer, ArrayList<Object>> getSaleDetails() {
        return saleDet;
    }
    public static void clean()
    {
        saleDet.clear();
    }
    public static void todaySaleDetails()
    {
        Date currentDate = new Date();
        String output = "Date : " + formattedDate + " Total Profit today : " + daySale;
        boolean flag = true;

        for (Map.Entry<Integer, ArrayList<Object>> m : Customer.getCustDetail().entrySet()) {
            flag = false;
            ArrayList<Object> dd = m.getValue();
            output += "\n Customer id : " + m.getKey() + " Name : " + dd.get(0) + " Purchased in our shop : " + dd.get(3);
        }

        if (flag) {
            output += "\n-- Today no sale in our shop --";
        }

        
        Writter.TotalSale(output);
    }




}

class Bill
{
    private Customer customer;
    private int billId;
    private float billAmount;
    private float totalDiscounted;
    public static int count=0;
    private static Map<Integer,ArrayList<Object>> billDet=new LinkedHashMap<>();
    static LocalDateTime myDateObj = LocalDateTime.now();
    static DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    static String formattedDate = myDateObj.format(myFormatObj);


    Bill(Customer custom) {
        Map <Integer,ArrayList<Object>> bill=Sale.getSaleDetails();
        for(Map.Entry<Integer,ArrayList<Object>> m: bill.entrySet())
        {
        this.billId=++count;
        ArrayList<Object> billObjects=m.getValue();
        totalDiscounted+=(float)billObjects.get(3);
        billAmount+=(float)billObjects.get(4);
        }

        ArrayList<Object> custObject=custom.getCustObjectDetails();
        custObject.add(billAmount);
        custom.setPurchasedAmount(custObject);
        
        System.out.println();
        System.out.println();
        String output=("\n ----------------- \n \n  \n Name of the Customer :"+custObject.get(0)+" \n Date & Time : "+ formattedDate +"\n --- purchased Products ---" );
        System.out.println("-----------------");
        System.out.println();
        System.out.println();
        System.out.println("Name of the Customer :"+custObject.get(0));
        System.out.println("Date & Time : "+ formattedDate);
        System.out.println("--- purchased Products ---");
        for(Map.Entry<Integer,ArrayList<Object>> m: bill.entrySet())
        {
            ArrayList<Object> billObjects=m.getValue();
            output+=("\n Name : "+billObjects.get(0)+ " QTY :"+billObjects.get(1)+" Price : "+billObjects.get(3));
            System.out.println("Name : "+billObjects.get(0)+ " QTY :"+billObjects.get(1)+" Price : "+billObjects.get(4));
        }
        output+=("\n Discounted : "+totalDiscounted+"Total Bill Price : "+billAmount+"\n \n "+"-----------------");
        System.out.println("Discounted : "+totalDiscounted);
        System.out.println("Total Bill Price : "+billAmount);
        System.out.println();
        System.out.println();
        System.out.println("-----------------");
        Writter.SalesFile(output);
    }
    Bill(int id)
    {
        Map <Integer,ArrayList<Object>> bill=Sale.getSaleDetails();
        for(Map.Entry<Integer,ArrayList<Object>> m: bill.entrySet())
        {
        this.billId=++count;
        ArrayList<Object> billObjects=m.getValue();
        totalDiscounted+=(float)billObjects.get(3);
        billAmount+=(float)billObjects.get(4);
        }

        ArrayList<Object> custObject=Customer.getCustObjectDetails(id);
        if(custObject==null)
        {
            System.out.println(" -- No user found --");
            Customer.getDetails();

        }
        else
        {
            float totalBillAmount=(float)custObject.get(3)+(float)billAmount;
            custObject.set(3,totalBillAmount);
        }
    }
}



class Customer
{
    private static Scanner sc=new Scanner(System.in);
    private int id;
    private String name;
    private String email;
    private int mobileNumber;
    private static int count=0;
    private static Map<Integer,ArrayList<Object>> customerDet=new LinkedHashMap<>();

    public static Customer getDetails()
    {
        System.out.println("Enter name the customer : ");
        String name=sc.next();
        System.out.println("Enter mobile number the customer : ");
        int mobile=sc.nextInt();
        sc.nextLine();
        System.out.println("Enter email the customer : ");
        String email=sc.nextLine();
        Customer custom=new Customer(name, mobile, email);
        return custom;        
    }
    Customer(String name,int mobile,String email)
    {
        this.id=++count;
        this.name=name;
        this.mobileNumber=mobile;
        this.email=email;
        ArrayList<Object> cuslist=new ArrayList<>();
        cuslist.add(this.name);
        cuslist.add(this.email);
        cuslist.add(this.mobileNumber);
        customerDet.put(this.id,cuslist);
    }

    public static void showCustomer()
    {
        for(Map.Entry<Integer,ArrayList<Object>> m:customerDet.entrySet())
        {
            ArrayList<Object> dd=m.getValue();
            System.out.println("Customer id : "+m.getKey()+" Name : "+dd.get(0)+" Email : "+dd.get(1)+" Mobile number : "+dd.get(2)+" Purchased in our shop : "+dd.get(3));
            
        }
    }

    public static Map<Integer,ArrayList<Object>> getCustDetail()
    {
        return customerDet;
    }

    public static void setCustomerDetails(int custid) {
        System.out.println("1. Change Name : ");
        System.out.println("2. Change Mobile no  : ");
        System.out.println("3. Change email : ");
        System.out.println("4. Change All : ");
        int custdetchangeid=sc.nextInt();
        if(custdetchangeid==1)
        {
            Customer.setName(custid);
        }
        else if(custdetchangeid==2)
        {
            Customer.setMobileNo(custid);
        }
        else if(custdetchangeid==3)
        {
            Customer.setEmail(custid);
        }
        else if(custdetchangeid==4)
        {
            Customer.setAllDeatils(custid);
        }
        else
        {
            System.out.println(" You entered wrong detail ");
        }
        

    }

    private static void setAllDeatils(int custid) {
        setName(custid);
        setMobileNo(custid);
        setEmail(custid);
    }

    private static void setEmail(int custid) {
        System.out.println(" Enter email : ");
        String email=sc.next();
        Map<Integer,ArrayList<Object>> custDetails=getCustDetail();
        ArrayList<Object> custDetail=custDetails.get(custid);
        custDetail.set(1,email);
        
    }

    private static void setMobileNo(int custid) {
        System.out.println(" Enter mobile no : ");
        int mobile=sc.nextInt();
        Map<Integer,ArrayList<Object>> custDetails=getCustDetail();
        ArrayList<Object> custDetail=custDetails.get(custid);
        custDetail.set(2,mobile);
    }

    private static void setName(int custid) {
        System.out.println(" Enter name : ");

        String name=sc.next();
        Map<Integer,ArrayList<Object>> custDetails=getCustDetail();
        ArrayList<Object> custDetail=custDetails.get(custid);
        custDetail.set(0,name);

    }
    public String getCustomerName()
    {
        return this.name;
    }
    public ArrayList<Object> getCustObjectDetails()
    {
        ArrayList<Object> obj=new ArrayList<>();
        obj.add(this.name);
        obj.add(this.mobileNumber);
        obj.add(this.email);
        return obj;
    }
    public void setPurchasedAmount(ArrayList<Object> a)
    {
        customerDet.put(this.id,a);
    }
    public static ArrayList<Object> getCustObjectDetails(int id)
    {
        if(customerDet.containsKey(id))
        {
            return customerDet.get(id);
        }
        else
        {
            System.out.println("-- customer id not found --");
        }
        return null;
        
    }   
}

class PremiumCustomer extends Customer
{

    PremiumCustomer(String name, int mobile, String email) {
        super(name, mobile, email);
    }
    
}

class Draiver{
    public static void cust()
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter id of which product You need one by one, if you select all products choose -1 to close");
        while(true)
        {
            int orderId=sc.nextInt();
            if(orderId==-1)
            {
                System.out.println("Press y for new Customer or otherwise n");
                sc.nextLine();
                char newCust=sc.next().charAt(0);
                if(newCust=='y' || newCust=='Y')
                {
                    Bill b=new Bill(Customer.getDetails());
                }
                else
                {
                    System.out.println("Enter Customer code : ");
                    int custCodeId=sc.nextInt();
                    Bill b=new Bill(custCodeId);
                    System.out.println("Thanks for purchase in our shop");

                }

                break;
            }
            else
            {
                Sale s=new Sale(orderId);
                Writter.writeDataToCSV("products.csv");
            }
        }
    }
    public static void main(String[] args)
    {
        Scanner sc=new Scanner(System.in);
        
        Writter.loadProductFromCsv();

        Product.getAllProudctDetails();
        System.out.println("Enter 1 for Immediate Sale ,2 for others ");
        
        int handle=sc.nextInt();
        System.out.println(handle);
        if(handle==1)
        {
            
            int custCount=0;
            int i=0;
            while(custCount!=-1)
            {   
                System.out.println("||||||||");
                System.out.println("For custermer : "+(++i));
                cust();
                Sale.clean();
                System.out.println("Enter -1 to close shop :");
                custCount=sc.nextInt();
                
                
            }
        }

        if(true)
        {
            System.out.println("1: Add product ");
            System.out.println("2: Remove product ");
            System.out.println("3: Update price ");
            System.out.println("4: Check Product ");
            System.out.println("5: New Sale ");

            System.out.println("6: Customer Details ");
            System.out.println("7: Update Customer Details ");
            System.out.println("8: Today total sale Details");
            
            System.out.println("Choose option");
            while(true)
            {
                System.out.println("--------");
                int x=sc.nextInt();
                if(x==1)
                {
                    System.out.println("Enter product name,qty,price,category ");
                    String name=sc.next();
                    // String name=sc.nextLine();
                    int qty=sc.nextInt();
                    int price=sc.nextInt();
                    int category=sc.nextInt();
                    new Product(name, qty, price, category);
                    System.out.println("If you want to see product List Enter 1 ");
                    int y=sc.nextInt();
                    if(y==1)
                    {
                        Product.getAllProudctDetails();
                    }

                    Writter.writeDataToCSV("products.csv");
                }
                else if(x==2)
                {
                    System.out.println("Enter id of the product to be remove : ");
                    int id=sc.nextInt();
                    Product.removeProductById(id);
                    System.out.println("If you want to see product List Enter 1 ");
                    int y=sc.nextInt();
                    if(y==1)
                    {
                        Product.getAllProudctDetails();
                    }

                    Writter.writeDataToCSV("products.csv");


                }
                else if(x==3)
                {
                    
                    System.out.println("Enter id of the product to be update : ");
                    int id=sc.nextInt();
                    Product.setProductPriceById(id);
                    System.out.println("If you want to see product List Enter 1 ");
                    int y=sc.nextInt();
                    if(y==1)
                    {
                        Product.getAllProudctDetails();
                    }

                    Writter.writeDataToCSV("products.csv");

                }
                else if(x==4)
                {
                    System.out.println("--- Available product Details ---");
                    Product.checkQuantity();
                }
                else if(x==5)
                {
                    
                    System.out.println("Enter how much custermer :");
                    int custCount=sc.nextInt();
                    for(int i=0;i<custCount;i++)
                    {
                        System.out.println("||||||||");
                        System.out.println("For custermer : "+(i+1));
                        cust();
                        Sale.clean();
                        
                    }
                }
                else if(x==6)
                {
                    System.out.println("--- customer Details ---");
                    Customer.showCustomer();
                }
                else if(x==7)
                {
                    System.out.println("--- customer Details ---");
                    Customer.showCustomer();
                    System.out.println("Enter customer code to edit :");
                    int custid=sc.nextInt();
                    Customer.setCustomerDetails(custid);

                }
                else if(x==8)
                {
                    System.out.println("--- Today Bill ---");
                    Sale.todaySaleDetails();
                }
                else
                {
                    break;
                }
            }

        }
    }
}