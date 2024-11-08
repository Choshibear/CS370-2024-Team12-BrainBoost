package com.brainboost;

public class Main 
{
    public static void main(String[] args)
    {
        Database db = new Database();
        String res = db.getUser("connor");
        System.err.println(res);
    }
}
