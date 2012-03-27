package de.minestar.therock.data;

import java.util.Arrays;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.therock.database.DatabaseHandler;

// Storing the block changes
public class BlockQueue {

    private DatabaseHandler dbHandler;

    private final static int BUFFER = 512;

    private BlockChange[] queue;

    private int pointer = 0;

    public BlockQueue(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        init();
    }

    private void init() {
        queue = new BlockChange[512];
        for (int i = 0; i < BUFFER; ++i)
            queue[i] = new BlockChange();
    }

    public void destroyedBlock(Block destroyedBlock, Player player) {
        BlockChange change = queue[pointer++];
        change.update(destroyedBlock, player);
        if (pointer == BUFFER)
            flush();
    }

    public void replacedBlock(Block replacedBlock, Block newBlock, Player player) {
        BlockChange change = queue[pointer++];
        change.update(replacedBlock, newBlock, player);
        if (pointer == BUFFER)
            flush();
    }

    public void flush() {
        BlockChange[] copy = Arrays.copyOfRange(queue, 0, pointer);
        dbHandler.flushBlockQueue(copy, pointer == BUFFER);
        pointer = 0;
    }
}
