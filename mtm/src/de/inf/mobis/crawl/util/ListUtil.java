package de.inf.mobis.crawl.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author w.posdorfer
 */
public class ListUtil
{

    static <K> void filter(List<K> liste, Filter f)
    {
        LinkedList<K> removelater = new LinkedList<K>();
        for (K s : liste)
        {
            if (f.remove(s))
            {
                removelater.add(s);
            }
        }
        liste.removeAll(removelater);
    }

    public interface Filter
    {
        public boolean remove(Object element);
    }

}
