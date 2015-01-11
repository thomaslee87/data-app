package com.intellbi.dataobject;

import java.util.Map;
import java.util.Set;

public class Consumer {

    private int userId;
    private boolean isPhotoConsumer;
    private boolean is4GPhotoConsumer;
    
    
    Map<String, Integer> userName2Id;
    Map<Integer, String> consumerId2Phone;
    Map<Integer, Set<Integer>> userConsumerSet;
}
