package org.androidtown.my_project;

import android.util.Log;

public class ParsingString {
    private String main_text;
    private String problem ="//";
    private String example1="//";
    private String example2="//";
    private String example3="//";
    private String example4="//";

    public ParsingString(String text){
        this.main_text = text;
        settext();
    }
    public ParsingString(ParsingString pstring){
        this.problem=pstring.getProblem();
        this.example1=pstring.getExample1();
        this.example2=pstring.getExample2();
        this.example3=pstring.getExample3();
        this.example4=pstring.getExample4();
    }

    private void settext(){
        int startnum=0;
        int endnum=0;
        int findpoint=0;
        endnum = main_text.indexOf("(A)")-1;
        problem = main_text.substring(startnum,endnum);
        problem = problem.replace("\n","");

        startnum = problem.indexOf(".")-1;
        findpoint = problem.indexOf(",");

        if( 48 <= (int)problem.charAt(startnum) && (int)problem.charAt(startnum) <= 57){
            problem = main_text.substring(startnum+2,endnum);
        }
        else if(findpoint<4 && findpoint!=-1){
            problem = main_text.substring(findpoint+2,endnum);
        }

        startnum = main_text.indexOf("(A)")+3;
        endnum = main_text.indexOf("(B)")-1;
        example1 = main_text.substring(startnum,endnum);
        example1=example1.replace("\n","");
        example1=example1.replace(" ","");

        startnum = main_text.indexOf("(B)")+3;
        endnum = main_text.indexOf("(C)")-1;
        example2 = main_text.substring(startnum,endnum);
        example2=example2.replace("\n","");
        example2=example2.replace(" ","");

        startnum = main_text.indexOf("(C)")+3;
        endnum = main_text.indexOf("(D)")-1;
        example3 = main_text.substring(startnum,endnum);
        example3=example3.replace("\n","");
        example3=example3.replace(" ","");

        startnum = main_text.indexOf("(D)")+3;
        example4 = main_text.substring(startnum);
        example4=example4.replace("\n","");
        example4=example4.replace(" ","");

        Log.d("ex1",problem);
        Log.d("ex2",example1);
        Log.d("ex3",example2);
        Log.d("ex4",example3);
        Log.d("ex5",example4);
    }

    public String getExample1() {
        return example1;
    }

    public String getExample2() {
        return example2;
    }

    public String getExample3() {
        return example3;
    }

    public String getExample4() {
        return example4;
    }

    public String getProblem() {
        return problem;
    }

    public String getMain_text() {
        return main_text;
    }
}
