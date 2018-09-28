package com.mikh.updown.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

import static com.mikh.updown.GameMain.PlayServicesFunction;

/**
 * Created by so_ni on 3/14/2018.
 */

public class GameManager {
    private static GameManager ourInstance = new GameManager();

    public GameData gameData;
    private Json json = new Json();
    private FileHandle fileHandle = Gdx.files.local("bin/GameData.json");

    // public int score;





    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
    }

    public void initializeGameData()
    {
        if(!fileHandle.exists())
        {
            gameData = new GameData();

            gameData.setHighScore(0);
            gameData.setStarCount(0);


            saveData();
        }
        else
        {
            loadData();
        }

    }

    public void saveData()
    {
        if(gameData!= null)
        {
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)),false);
          //  fileHandle.writeString(json.prettyPrint(gameData),false);

        }
    }

    public void loadData()
    {
       gameData = json.fromJson(GameData.class,Base64Coder.decodeString(fileHandle.readString()));
       // gameData = json.fromJson(GameData.class,fileHandle.readString());

    }

    public void checkHighScore(int score)
    {

        //pass starCount as parameter in this method if you nedd it
        int oldHighScore = gameData.getHighScore();
        if(oldHighScore<score)
        {

            PlayServicesFunction.updateLeaderboards(score);
            gameData.setHighScore(score);

        }
      //  gameData.setStarCount(starCount);
        saveData();
    }

    public int getPresentHighScore()

    {
        return gameData.getHighScore();
    }

  /*  public int getPresentStarCount()
    {
        return  gameData.getStarCount();
    }


*/

}
