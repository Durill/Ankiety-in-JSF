package com.example.jsffff;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@ManagedBean
@SessionScoped
public class User {

    private int id=0;
    private String name;
    private String email;

    private Integer numberOfAnswers=1;
    private String answer1;
    private Integer quantity1=0;
    private String answer2;
    private Integer quantity2=0;
    private String answer3;
    private Integer quantity3=0;
    private String answer4;
    private Integer quantity4=0;
    private String answer5;
    private Integer quantity5=0;
    private String answersToAdd;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setNumberOfAnswers(Integer numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }

    public Integer getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setQuantity1(Integer quantity1) {
        this.quantity1 = quantity1;
    }

    public Integer getQuantity1() {
        return quantity1;
    }

    public void setQuantity2(Integer quantity2) {
        this.quantity2 = quantity2;
    }

    public Integer getQuantity2() {
        return quantity2;
    }

    public void setQuantity3(Integer quantity3) {
        this.quantity3 = quantity3;
    }

    public Integer getQuantity3() {
        return quantity3;
    }

    public void setQuantity4(Integer quantity4) {
        this.quantity4 = quantity4;
    }

    public Integer getQuantity4() {
        return quantity4;
    }

    public void setQuantity5(Integer quantity5) {
        this.quantity5 = quantity5;
    }

    public Integer getQuantity5() {
        return quantity5;
    }

    public void setAnswersToAdd(String answersToAdd) {
        this.answersToAdd = answersToAdd;
    }

    public String getAnswersToAdd() {
        return answersToAdd;
    }

    public String addUser(){
        Connection connection=null;
        try{
            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();
            String sql = "INSERT INTO user(id, name, email,numberOfAnswers) VALUES('"+id+"','"+name+"','"+email+"','"+numberOfAnswers+"')";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            return "modify";
        }catch (Exception e){
            System.out.println(e);
            return "error";
        }finally {
            try{
                if (connection!=null){
                    connection.close();
                }
            }catch (SQLException se){
                se.printStackTrace();
                return "error";
            }
        }
    }

    public ArrayList getAllAnswers() throws Exception{
        ArrayList listOfAnswers = new ArrayList();
        Connection connection = null;
        try {

            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();
            String sql = ("SELECT u.answer1, u.answer2, u.answer3, u.answer4, u.answer5 FROM user u WHERE name='"+name+"'");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                listOfAnswers.add(rs.getString("answer1"));
                listOfAnswers.add(rs.getString("answer2"));
                listOfAnswers.add(rs.getString("answer3"));
                listOfAnswers.add(rs.getString("answer4"));
                listOfAnswers.add(rs.getString("answer5"));
            }
            listOfAnswers.removeIf(Objects::isNull);
        }catch (Exception e){
            System.out.println(e);
        }finally {
            try{
                if (connection!=null){
                    connection.close();
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
        }
        return listOfAnswers;
    }

    private String amountOfAnswersInList;

    public void setAmountOfAnswersInList() throws Exception {
        Integer aaf = getAllAnswers().size()+1;
        String amount = Integer.toString(aaf);
        this.amountOfAnswersInList =amount;
    }

    public String getAmountOfAnswersInList() throws Exception {
        setAmountOfAnswersInList();
        return amountOfAnswersInList;
    }

    public String addAnswers(){
        Connection connection = null;
        try {
            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();
            Integer count = Integer.parseInt(amountOfAnswersInList);
            String nameOfColumn;
            switch (count){
                case 1: nameOfColumn = "answer1";
                    break;
                case 2: nameOfColumn = "answer2";
                    break;
                case 3: nameOfColumn = "answer3";
                    break;
                case 4: nameOfColumn = "answer4";
                    break;
                case 5: nameOfColumn = "answer5";
                    break;
                default: nameOfColumn = null;
            }
            String query = "UPDATE user SET "+nameOfColumn+"='"+answersToAdd+"' WHERE name='"+name+"'";
            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            return "modify";
        }catch (Exception e){
            System.out.println(e);
            return "error";
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public String deleteAnswer(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        Integer field_vs_index = Integer.parseInt(params.get("action"))+1;
        Connection connection = null;
        Boolean answersToDowngrade = false;
        try{
            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();
            String answerToDelete;
            switch (field_vs_index){
                case 1: answerToDelete = "answer1";
                break;
                case 2: answerToDelete = "answer2";
                break;
                case 3: answerToDelete = "answer3";
                break;
                case 4: answerToDelete = "answer4";
                break;
                case 5: answerToDelete = "answer5";
                break;
                default: answerToDelete = null;
            }

            if(field_vs_index<getAllAnswers().size()+1){
                answersToDowngrade = true;
            }

            String query = "UPDATE user SET "+answerToDelete+"=null WHERE name='"+name+"'";
            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

            if(answersToDowngrade){
                String answersAbove="";
                String deleteLast = "answer5=null";
                for(int i=field_vs_index;i<5;i++){
                    int j = i+1;
                    answersAbove+="answer"+i+"=answer"+j+", ";
                    System.out.println(answersAbove);
                }
                String downgradeQuery = "UPDATE user SET "+answersAbove+deleteLast+" WHERE name='"+name+"'";
                System.out.println(downgradeQuery);
                PreparedStatement statementDowngrade = connection.prepareStatement(downgradeQuery);
                statementDowngrade.executeUpdate();
            }

        }catch (Exception e){
            System.out.println(e);
            return "error";
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return "modify";
    }

    public void checkWhichAnswerIsLast(Integer numberofAnswerToDelete){

    }

    public User(){}

}
