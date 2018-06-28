package com.example.shilpipandey.ruzzle;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Handler;

public class boardActivity extends AppCompatActivity {
    private int gridSize = 4;
    private String[] gridCharacters=new String[16];
    private GridView gridView;
    private HashMap<Integer, ArrayList<Integer>> adjacency = new HashMap<>();
    private static int[] neighbours = {-1, 1, -3, 3, -4, 4, -5, 5};
    private static int[] neighboursFirstCol = {1, -3, -4, 4, 5};
    private static int[] neighboursLastCol = {-1, 3, -4, 4, -5};
    private HashMap<Integer, Integer> visited = new HashMap<>();
    private int validWordCount=0;
    private trieDictionary dictionary ;
    private int score=0;
    private String userWord=new String();
    private HashSet<String> visitedWords=new HashSet<>();
    private HashSet<String> validBoardWords=new HashSet<>();
    private int MINIMUM_WORDS=70;
    private HashSet<Integer> userMoves=new HashSet<>();
    //private TextView userWordCount = (TextView)findViewById(R.id.userWordsCount);
    private int lastValidPosition=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        gridView = (GridView) findViewById(R.id.board);
        TextView userWordCount = (TextView)findViewById(R.id.userWordsCount);
        userWordCount.setText("0");
        createDictionary();
        getGoodBoard();
        //gridCharacters = new String[]{"t","d","y","o","e","s","i","f","o","e","t","f","m","l","r","a"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, gridCharacters);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isValidMove(position)) {
                    userWord += gridCharacters[position];
                    userMoves.add(position);
                    TextView userWordView = (TextView) findViewById(R.id.userWord);
                    userWordView.setText(userWord);
                    lastValidPosition=position;
                }
                else {
                    Toast toast = Toast.makeText(boardActivity.this, "Invalid Move", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        Log.i("done", "..");

    }
    public void getGoodBoard()
    {
        Random randomChar = new Random();
        while(true) {
            validWordCount=0;
            validBoardWords.clear();
            Log.i("newBoard","yeh aayaaaa");
            for (int i = 0; i < gridSize * gridSize; i++) {
                int temp = randomChar.nextInt(26) + 97;
                char temp2 = (char) temp;
                gridCharacters[i] = Character.toString(temp2);
            }
            getNeighbours();
            dfsVisit();
            if(validWordCount>=MINIMUM_WORDS)
                break;
        }
    }
    public void createDictionary(){
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new trieDictionary(new InputStreamReader(inputStream));
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void getNeighbours()
    {
        adjacency.clear();
        for(int i=0;i<gridCharacters.length;i++) {
            ArrayList<Integer> temp=new ArrayList<>();
            if(i%4==0) {
                for (int j = 0; j < neighboursFirstCol.length; j++) {
                    if(i+neighboursFirstCol[j]>=0 && i+neighboursFirstCol[j]<gridSize*gridSize)
                    {
                        temp.add(i+neighboursFirstCol[j]);
                    }
                }
            }
            else if(i%4==3)
            {
                for (int j = 0; j < neighboursLastCol.length; j++) {
                    if(i+neighboursLastCol[j]>=0 && i+neighboursLastCol[j]<gridSize*gridSize)
                    {
                        temp.add(i+neighboursLastCol[j]);
                    }
                }
            }
            else
            {
                for (int j = 0; j < neighbours.length; j++) {
                    if(i+neighbours[j]>=0 && i+neighbours[j]<gridSize*gridSize)
                    {
                        temp.add(i+neighbours[j]);
                    }
                }
            }
            adjacency.put(i,temp);
        }
        for(int i=0;i<gridCharacters.length;i++) {
            ArrayList<Integer> temp=adjacency.get(i);
            Log.i("Adjacency","adjacency for:"+Integer.toString(i));
            for(int j=0;j<temp.size();j++)
            {
                Log.i("adj",Integer.toString(temp.get(j)));
            }
        }
        dfsVisit();
    }
    public void dfsVisit()
    {
        for(int i=0;i<gridSize*gridSize;i++) {
            visited.clear();
            visited.put(i,1);
            //Log.i("Visitings DFS for ",Integer.toString(i));
            String word=gridCharacters[i];
            dfs(i,word);
        }
        //Log.i("count",Integer.toString(validWordCount));
        TextView totalWordCount = (TextView)findViewById(R.id.totalWords);
        totalWordCount.setText(Integer.toString(validWordCount));
    }

    public void dfs(int i,String word) {
        int searchResult = dictionary.search(word);
        if(searchResult==1 && word.length()!=1 && !validBoardWords.contains(word)) {
            validWordCount++;
            validBoardWords.add(word);
            Log.i("iyvisiting Word ", word);
        }
        else if(searchResult==0)
            return;
        if(word.length()>10)
            return;
        ArrayList<Integer> neighbours = adjacency.get(i);
        for(int j=0;j<neighbours.size();j++) {
            if(visited.get(neighbours.get(j))==null) {
                visited.put(neighbours.get(j),1);
                //Log.i("Visitings j",Integer.toString(neighbours.get(j)));
                dfs(neighbours.get(j),word+gridCharacters[neighbours.get(j)]);
                //visited.clear();
                visited.remove(neighbours.get(j));
            }
        }
    }

    public void isValidWord(View v) {
        if(validBoardWords.contains(userWord)&&!visitedWords.contains(userWord)) {
            score++;
            visitedWords.add(userWord);
            Toast toast = Toast.makeText(this, "Correct word", Toast.LENGTH_LONG);
            toast.show();
            TextView userWordCount = (TextView)findViewById(R.id.userWordsCount);
            userWordCount.setText(Integer.toString(score));
        }
        else if(visitedWords.contains(userWord))
        {
            Toast toast = Toast.makeText(this, "Repeated word", Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText(this, "Invalid word", Toast.LENGTH_LONG);
            toast.show();
        }
        userWord="";
        TextView userWordView =(TextView)findViewById(R.id.userWord);
        userWordView.setText(userWord);
        userMoves.clear();
        Log.i("word was:",userWord);
    }

    public void resetWord(View v) {
        userWord="";
        TextView userWordView =(TextView)findViewById(R.id.userWord);
        userWordView.setText(userWord);
        userMoves.clear();
    }
    public boolean isValidMove(int currentPosition)
    {
        if(userWord.length()==0) {
            return true;
        }
        else
        {
            ArrayList<Integer> possibleMoves=new ArrayList<>();
            possibleMoves=adjacency.get(lastValidPosition);
            if(userMoves.contains(currentPosition))
                return false;
            for(int i=0;i<possibleMoves.size();i++)
            {
                if(possibleMoves.get(i)==currentPosition)
                    return true;
            }
        }
        return false;
    }
    public void solveBoard(View v) {
        String []words=new String[validBoardWords.size()];
        validBoardWords.toArray(words);
        Intent intent=new Intent(this,solverActivity.class);
        Bundle bundle=new Bundle();
        bundle.putStringArray("validBoardWords",words);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
