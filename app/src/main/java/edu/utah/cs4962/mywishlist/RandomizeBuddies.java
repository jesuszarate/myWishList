package edu.utah.cs4962.mywishlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Jesus Zarate on 12/7/14.
 */
public class RandomizeBuddies
{
    ArrayList<Integer> taken = new ArrayList<Integer>();
    Random randomGenerator = new Random(System.currentTimeMillis());

    public HashMap<Buddy, Buddy> randomizer(ArrayList<Buddy> buddyList)
    {
        HashMap<Buddy, Buddy> buddyPairing = new HashMap<Buddy, Buddy>();

        for (Buddy buddy : buddyList)
        {
            int buddyId = getRandomBuddy(buddyList.size());

            Buddy givee = buddyList.get(buddyId);

            while (givee.equals(buddy))
            {
                givee = buddyList.get(buddyId);
            }
            buddyPairing.put(buddy, givee);
            taken.add(buddyId);
        }
        return buddyPairing;
    }

    public int getRandomBuddy(int listSize)
    {
        int randomNum = randomGenerator.nextInt(listSize);

        while (taken.contains(randomNum))
        {
            randomNum = randomGenerator.nextInt(listSize);
        }
        return randomNum;
    }

}
