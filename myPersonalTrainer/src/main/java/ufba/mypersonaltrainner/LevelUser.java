package ufba.mypersonaltrainner;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LevelUser {
    private static LevelUser instance;
    private static final int RATE_EXP = 100; //Constante que define a exp maxima

    private int level;
    private int pontos;
    private int maxpontos;

    private LevelUser(){
        ParseUser user = ParseUser.getCurrentUser();

        level = user.getInt("level");
        pontos = user.getInt("pontos");
        if(level < 1){ //Valor indefinido
            user.put("level",1);
            user.put("pontos",0);
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

        user.put("level", level);
        user.put("pontos", aux);
        user.saveInBackground();

    }

    public int getMaxpontos() { return maxpontos;   }

}