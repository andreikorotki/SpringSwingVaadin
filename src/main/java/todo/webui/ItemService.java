package todo.webui;

import java.util.ArrayList;
import java.util.Set;

public interface ItemService {
    void delete(Item item);
    void save(Item item);
    public  ArrayList<Item> findAll();
    void addEmptyItem();
    void deleteItems(Set<Item> items);


}

