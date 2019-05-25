package luceneproj;

import java.util.Comparator;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////USED FOR SORTING WRT POSTINGS SIZE////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ArrayIndexComparator implements Comparator<Integer>
{
    private final int[] array;

    public ArrayIndexComparator(int[] array)
    {
        this.array = array;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
        //return array[index1].compareTo(array[index2]);
        return Integer.compare(array[index1], array[index2]);
    }
}
