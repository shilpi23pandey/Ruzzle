import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Shilpi Mishra on 30-03-2018.
 */

public class board {
    private int gridSize = 4;
    private ArrayList<Integer> grid = new ArrayList<>();
    public void createBoard(){
        Random randomChar = new Random();
        for(int i=0;i<gridSize*gridSize;i++) {
            int temp = randomChar.nextInt(26);
            grid.add(temp);
        }

    }
}
