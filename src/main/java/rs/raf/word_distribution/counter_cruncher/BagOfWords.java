package rs.raf.word_distribution.counter_cruncher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BagOfWords extends ArrayList<String> {

    @Override
    public boolean add(String string) {
        int index = Collections.binarySearch(this, string);
        if (index < 0) index = ~index;
        super.add(index, string);
        return true;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (o == this)
//            return true;
//        if (!(o instanceof List))
//            return false;
//
//        List<String> l1 = this.stream().sorted().collect(Collectors.toList());
//        List<String> l2 = ((BagOfWords)o).stream().sorted().collect(Collectors.toList());
//
//        return l1.equals(l2);
//    }
//
//    @Override
//    public int hashCode() {
//        return this.stream().sorted().collect(Collectors.toList()).hashCode();
//    }
}
