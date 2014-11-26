package ufba.mypersonaltrainner;

import com.parse.ParseUser;

import ufba.mypersonaltrainner.util.PK;

public class LevelUser {
    private static LevelUser instance;
    private static final int RATE_EXP = 100; // Exp maxima

    private int level;
    private int pontos;
    private int maxpontos;

    private LevelUser(){
        ParseUser user = ParseUser.getCurrentUser();

        level = user.getInt(PK.USER_LEVEL);
        pontos = user.getInt(PK.USER_PONTOS);

        if(level < 1){ //Valor indefinido
            user.put(PK.USER_LEVEL,1);
            user.put(PK.USER_PONTOS,0);
            user.saveInBackground();
            level = 1;
        }
        maxpontos = level*RATE_EXP;
    }

    public static LevelUser getInstance(){
        if (instance == null) {
            instance = new LevelUser();
        }
        return instance;
    }

    public int getLevel() {
        return level;
    }

    public int getPontos() {
        return pontos;
    }

    public void addPontos(int pontos){ // Versão 1
        ParseUser user = ParseUser.getCurrentUser();

        int aux = (this.pontos + pontos)%maxpontos;
        if(aux < this.pontos) { //Caso os novos pontos sejam menor do que o anterior, provavelmente ele upou
            level++;
            maxpontos = level*RATE_EXP;
        }
        this.pontos = aux;

        user.put(PK.USER_LEVEL, level);
        user.put(PK.USER_PONTOS, aux);
        user.saveInBackground();
    }

    public int getMaxpontos() { return maxpontos;   }

}