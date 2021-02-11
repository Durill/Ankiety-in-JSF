package com.example.jsffff;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ManagedBean
@SessionScoped
public class User {

    private int id=0;

    @Size(min = 3)
    private String name;

    @Email
    @NotEmpty
    private String email;

    private String answersToAdd;

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

            String sql = "INSERT INTO user(id, name, email) VALUES('"+id+"','"+name+"','"+email+"')";
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

    public ArrayList getAllAnswers(){
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

    public ArrayList getAllQuantities(){
        ArrayList listOfQuantities = new ArrayList();
        Connection connection = null;
        try {

            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();
            String sql = ("SELECT u.quantity1, u.quantity2, u.quantity3, u.quantity4, u.quantity5 FROM user u WHERE name='"+name+"'");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                listOfQuantities.add(rs.getInt("quantity1"));
                listOfQuantities.add(rs.getInt("quantity2"));
                listOfQuantities.add(rs.getInt("quantity3"));
                listOfQuantities.add(rs.getInt("quantity4"));
                listOfQuantities.add(rs.getInt("quantity5"));
            }
            int answersInListForQuantities = getAllAnswers().size();
            for (int i = 4;i>=answersInListForQuantities;i--){
                listOfQuantities.remove(i);
            }

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
        return listOfQuantities;
    }

    private String amountOfAnswersInList;

    public void setAmountOfAnswersInList() throws Exception {
        String amount = Integer.toString(getAllAnswers().size()+1);
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
            String query = "UPDATE user SET "+extractAnswerFromMappedNumber(count)+"='"+answersToAdd+"' WHERE name='"+name+"'";
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
        int field_vs_index = Integer.parseInt(params.get("action"))+1;
        Connection connection = null;
        boolean answersToDowngrade = false;
        try{
            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();

            //checking if future deleted answer is inside section and need to be replaced
            //by higher number answer
            if(field_vs_index<getAllAnswers().size()+1){
                answersToDowngrade = true;
            }

            String query = "UPDATE user SET "+extractAnswerFromMappedNumber(field_vs_index)+"=null WHERE name='"+name+"'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

            //replacing deleted answer with a higher answer number
            if(answersToDowngrade){
                String answersAbove="";
                String deleteLast = "answer5=null";
                for(int i=field_vs_index;i<5;i++){
                    int j = i+1;
                    answersAbove+="answer"+i+"=answer"+j+", ";
                }
                String downgradeQuery = "UPDATE user SET "+answersAbove+deleteLast+" WHERE name='"+name+"'";
                PreparedStatement statementDowngrade = connection.prepareStatement(downgradeQuery);
                statementDowngrade.executeUpdate();
                String resetQuantities = "UPDATE user SET quantity1=0, quantity2=0, quantity3=0, quantity4=0, quantity5=0 WHERE  name='"+name+"'";
                PreparedStatement statementReset = connection.prepareStatement(resetQuantities);
                statementReset.executeUpdate();
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

    private String answerToEdit;

    public void setAnswerToEdit(String answerToEdit) {
        this.answerToEdit = answerToEdit;
    }

    public String getAnswerToEdit() {
        return answerToEdit;
    }

    private String answerNumberToEdit;

    public String getAnswerNumberToEdit() {
        return answerNumberToEdit;
    }

    public String editAnswer(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        int getAnswerNumber = Integer.parseInt(params.get("action"))+1;
        answerNumberToEdit = Integer.toString(getAnswerNumber);
        String answerToFind = "u.answer"+getAnswerNumber;
        Connection connection = null;
        try {
            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();
            String sql = ("SELECT "+answerToFind+" FROM user u WHERE name='"+name+"'");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                answerToEdit = rs.getString("answer"+getAnswerNumber);
            }

            return "edit";
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

    public String updateAnswer(){
        Connection connection = null;
        try {
            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();
            String ans = "answer"+answerNumberToEdit;
            String query = "UPDATE user SET "+ans+"='"+answerToEdit+"' WHERE name='"+name+"'";
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

    Integer answerForVote;

    public void setAnswerForVote() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        answerForVote = Integer.parseInt(params.get("action"))+1;
    }

    public String voteForAnswer(){
        String ansSelect = "u.quantity"+answerForVote;
        String ansUpdate = "quantity"+answerForVote;
        int currentQuantity=0;
        Connection connection = null;
        try {
            DB_connection db_connection = new DB_connection();
            connection = db_connection.getConnection();

            String querySelect = "SELECT "+ansSelect+" FROM user u WHERE name='"+name+"'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(querySelect);
            while (rs.next()){
                currentQuantity = rs.getInt("quantity"+answerForVote);
            }
            currentQuantity=currentQuantity+1;

            String queryUpdate = "UPDATE user SET "+ansUpdate+"='"+currentQuantity+"' WHERE name='"+name+"'";
            PreparedStatement statementUpdate = connection.prepareStatement(queryUpdate);
            statementUpdate.executeUpdate();

            return "aftervote";
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


    private String extractAnswerFromMappedNumber(Integer mappedNumber) {
        String answerToDelete;
        switch (mappedNumber){
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
        return answerToDelete;
    }

    public User(){}

}
