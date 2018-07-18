package com.android.gapsproject;

/**
 * Created by Eng_S on 1/22/2018.
 */

public class Data {
    //boolean varaible to check if the login is sucess
    private  boolean login  ;
    // variables for the user inforamtion for register and login
    private String fname , lname , username , password ;
    //varable when the user chooese the location
    private  String address;
    //varaibles for lang and lat
    double lat , lang;
     public   Data ()

     {
         this.login = isLogin();
         this.username = getUsername();
         this.password = getPassword();
         this.fname = getFname();
         this.lname =getLname();
         this.address = getAddress();
         this.lang =getLang() ;
         this.lat = getLat();
     }



     public  void setLat (double lat)
     {
         this.lat = lat ;
     }

     public void setLang (double lang)
     {
         this.lang = lang;
     }

     public  double getLat ()
     {
         return  lat ;
     }

     public  double getLang ()

     {
         return  lang;
     }
    public  void  setLogin (boolean login)
    {
        this.login = login ;


    }


    public  void setFname (String fname)
    {
        this.fname = fname;
    }


    public  void setLname (String lname)
    {
        this.lname = lname;
    }

    public  void setUsername (String username)
    {
         this.username = username ;

    }

    public  void setPassword (String password)
    {
        this.password = password;
    }


    public  void setAddress (String address)
    {
        this.address = address;
    }


    public  boolean isLogin ()
    {
        return login;
    }

    public  String getFname ()
    {
         return fname;
    }

    public  String getLname ()
    {
        return lname;
    }

    public String getUsername ()
    {
        return username;
    }

    public  String getPassword ()

    {
        return password;
    }

    public  String getAddress ()
    {
        return  address;
    }
}
