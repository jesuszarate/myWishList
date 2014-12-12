package edu.utah.cs4962.mywishlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Jesus Zarate on 12/7/14.
 */
public class RandomizeBuddies
{
    ArrayList<Buddy> hatContents = new ArrayList<Buddy>();
    ArrayList<Integer> taken = new ArrayList<Integer>();
    Random randomGenerator = new Random(System.currentTimeMillis());

    public HashMap<Buddy, Buddy> randomizer(ArrayList<Buddy> buddyList)
    {
        hatContents.addAll(buddyList);

        //hatContents = buddyList;
        HashMap<Buddy, Buddy> buddyPairing = new HashMap<Buddy, Buddy>();

        //for (Buddy buddy : buddyList)
        for(int buddyIndex = 0; buddyIndex < buddyList.size(); buddyIndex++)
        {
            Buddy buddy = buddyList.get(buddyIndex);
//            int buddyId = getRandomBuddy(buddyList.size() - taken.size());
            int buddyId = getRandomBuddy(hatContents.size());

            Buddy givee = hatContents.get(buddyId);

            while (givee.equals(buddy) ||
                    (buddyPairing.containsKey(givee) && buddyPairing.get(givee).equals(buddy)))
            {
                buddyId = getRandomBuddy(hatContents.size());//buddyList.get(buddyId);

                givee = hatContents.get(buddyId);
            }

            hatContents.remove(givee);
            buddyPairing.put(buddy, givee);

            taken.add(buddyId);
        }


        return buddyPairing;
    }

    public int getRandomBuddy(int listSize)
    {
        int randomNum = randomGenerator.nextInt(listSize);

//        while (taken.contains(randomNum))
//        {
//            randomNum = randomGenerator.nextInt(listSize);
//        }
        return randomNum;
    }

}
