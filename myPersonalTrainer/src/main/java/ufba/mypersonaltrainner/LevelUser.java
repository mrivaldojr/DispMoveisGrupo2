package ufba.mypersonaltrainner;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Welbert on 23/11/2014.
 */
public class LevelUser {
    private static LevelUser instance;
    private static String id_user;
    private static final int RATE_EXP = 100; //Constante que define a exp maxima

    private ParseObject LevelTable;
    private int level;
    private int pontos;
    private int maxpontos;

    private LevelUser(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LVL_level");
        query.whereEqualTo("lvl_id_usuario", id_user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject Object, com.parse.ParseException e) {
                if (e == null) {
                    level = Object.getInt("lvl_nu_level");
                    pontos = Object.getInt("lvl_nu_pontos");
                    maxpontos = level*RATE_EXP;
                    LevelTable = Object; // Salva o objeto pra realizar o update em 'addpontos()'
                } else {
                    level = 1;
                    pontos = 0;
                    maxpontos = RATE_EXP;
                    ParseObject LevelTable = new ParseObject("LVL_level");
                    LevelTable.put("lvl_nu_level", 1);
                    LevelTable.put("lvl_nu_pontos",0);
                    LevelTable.put("lvl_id_usuario", id_user);
                    LevelTable.saveInBackground();
                }
            }
        });
    }

    public static LevelUser getInstance(){
        if (instance == null) {
            instance = new LevelUser();
        }
        return instance;
    }

    public static void setId_userd_user(String arg_id_user){
        id_user = arg_id_user;
    }

    public int getLevel() {
        return level;
    }

    public int getPontos() {
        return pontos;
    }

    public void addPontos(int pontos){ // Versão 1
        int aux = (this.pontos + pontos)%maxpontos;
        if(aux < this.pontos) { //Caso os novos pontos sejam menor do que o anterior, provavelmente ele upou
            level++;
            maxpontos = level*RATE_EXP;
        }
        this.pontos = aux;

        LevelTable.put("lvl_nu_level", level);
        LevelTable.put("lvl_nu_pontos",pontos);
        LevelTable.saveInBackground();
    }

    public int getMaxpontos() { return maxpontos;   }
}
