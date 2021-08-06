package org.servlet.test;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Payslip extends HttpServlet{
    //class variables
    String firstName,lastName;
    int grossIncome, monthlyTax, monthlyGross, monthlyNet, monthlySuper;
    double superRate;
    Date startDate, endDate;


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        //collect variables

        String fname = req.getParameter("userFname");
        String lName = req.getParameter("userLname");
        int salary = Integer.parseInt(req.getParameter("userSalary"));
        double customSuperRate = Double.parseDouble(req.getParameter("userSuperRate"));
        String date = req.getParameter("userStartDate");

        //build object (tried using constructor but there were errors)
        setFirstName(fname);
        setLastName(lName);
        grossIncome = salary;
        superRate = customSuperRate;
        try {
            setStartDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setPaymentPeriod(startDate);
        setMonthlyGross();
        calculateMonthlyTax(grossIncome);
        setMonthlyNet();
        calculateSuper();

        //html response block
        resp.getWriter().println("<html>");
        resp.getWriter().println("<head>");
        resp.getWriter().println("<title>Payslip Result</title>");
        resp.getWriter().println("</head>");
        resp.getWriter().println("<body>");
        //output
        resp.getWriter().println("Name: " + firstName + " " + lastName + "<br>"+ "<br>");
        resp.getWriter().println("Payment period: " + startDate + " TO " + endDate + "<br>"+ "<br>");
        resp.getWriter().println("Gross income($): " + monthlyGross + "<br>"+ "<br>");
        resp.getWriter().println("Tax deducted($): " + monthlyTax + "<br>"+ "<br>");
        resp.getWriter().println("Net income($): " + monthlyNet + "<br>"+ "<br>");
        resp.getWriter().println("Super amount($): " + monthlySuper );


        resp.getWriter().println("</body>");
        resp.getWriter().println("</html>");


    }

    public Payslip (String first_name, String last_name, int annual_salary, double super_rate, String start_date) throws ParseException {
        setFirstName(first_name);
        setLastName(last_name);
        grossIncome = annual_salary;
        superRate = super_rate;
        setStartDate(start_date);
        setPaymentPeriod(startDate);
        setMonthlyGross();
        calculateMonthlyTax(grossIncome);
        setMonthlyNet();
        calculateSuper();

    }
    public Payslip(){}

    public void setFirstName(String fName){
        firstName = fName;
    }
    public void setLastName(String lName){
        lastName = lName;
    }
    public void setStartDate(String date) throws ParseException {
        startDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
    }
    public void setMonthlyGross(){
        monthlyGross = Math.round(grossIncome/12);
    }
    public void setMonthlyNet(){
        monthlyNet = monthlyGross - monthlyTax;
    }

    public void calculateSuper(){
        monthlySuper = (int) Math.round(superRate * monthlyGross);
    }

    public void calculateMonthlyTax(int grossIncome){
        int retVal = 0; Double tempTax = null;
        int finalTax;
        if(grossIncome < 18200){
            tempTax = 0.00;
        }
        else if(grossIncome > 18200 && grossIncome <= 37000){
            int surplus = grossIncome - 18200;
            tempTax = 0.19 * surplus;
        }
        else if(grossIncome > 37000 && grossIncome <= 87000){
            int surplus = grossIncome - 37000;
            tempTax = (0.325 * surplus) + 3572;
        }
        else if(grossIncome > 87000 && grossIncome <= 180000){
            int surplus = grossIncome - 87000;
            tempTax = (0.37 * surplus) + 19822;
        }
        else if(grossIncome > 180000){
            int surplus = grossIncome - 180000;
            tempTax = (0.45 * surplus) + 54232;
        }
        finalTax = (int)Math.round(tempTax/12);
        monthlyTax = finalTax;
    }

    public void setPaymentPeriod(Date startDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
// manipulate date
        cal.add(Calendar.MONTH, 1);
// convert calendar to date
        Date modifiedDate = cal.getTime();
        endDate = modifiedDate;
    }

}

