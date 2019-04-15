package todo.webui;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemServiceImpl  implements ItemService{

    private static ItemServiceImpl instance;

    private  HashMap<Long, Item> itemMap;
    private long nextId = 10;
    private ItemServiceImpl() {
    }



    public static ItemServiceImpl getInstance() {
        if (instance == null) {
            instance = new ItemServiceImpl();
            //instance.ensureTestData();
        }
        return instance;
    }

    public void setItemMap(HashMap itemMap) {
        this.itemMap = itemMap;
    }

    @Override
    public synchronized void save(Item entry) {
        if (entry == null) {
            return;
        }
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {

            entry = (Item) entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        itemMap.put(entry.getId(), entry);
    }

    @Override
    public synchronized void delete(Item item) {
        itemMap.remove(item.getId());
    }


    @Override
    public synchronized void deleteItems(Set<Item> items) {
        for (Item item:items
             ) {
            itemMap.remove(item.getId());

        }
    }


    public void addEmptyItem() {

        Item item = new Item();//(Item) context.getBean("t1000");
        item.setId(nextId++);
        item.setItemContent("New Item");

        itemMap.put(item.getId(),item);

    }


    public synchronized ArrayList<Item> findAll(){return findAll(null);}

    public synchronized ArrayList<Item> findAll(String stringFilter) {
        ArrayList<Item> arrayList = new ArrayList<>();

        for (Item contact : itemMap.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ItemService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Item>() {

            @Override
            public int compare(Item o1, Item o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        return arrayList;
    }



}
