package todo.webui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Route("")
@PWA(name = "Project Base for Vaadin Flow", shortName = "Project Base")
public class MainWebView extends VerticalLayout {
    ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("webapp.xml");

    private ItemService service = (ItemServiceImpl) ctx.getBean("itemService");
    private Notification notification = (Notification) ctx.getBean("notification");
    private Grid<Item> grid = new Grid<>(Item.class);


    public MainWebView() {
        Binder<Item> binder = new Binder<>(Item.class);
        Editor<Item> editor = grid.getEditor();
        editor.setBinder(binder);

        TextField field = (TextField) ctx.getBean("textField");
        Button addItemButton = (Button) ctx.getBean("buttonAdd");
        Button deleteItemBtn = (Button) ctx.getBean("buttonDlt");
        HorizontalLayout toolbar = new HorizontalLayout(addItemButton, deleteItemBtn);
        HorizontalLayout mainContent = new HorizontalLayout(grid);
        HorizontalLayout notificationbar = new HorizontalLayout(notification);


        addItemButton.addClickListener(clickEvent -> {
                    service.save(binder.getBean());
                    service.addEmptyItem();
                    updateList();
                });

        deleteItemBtn.addClickListener(buttonClickEvent -> {
            service.save(binder.getBean());
            service.deleteItems(grid.getSelectedItems());
            updateList();
        });


        grid.setColumns("itemContent");
        // Close the editor in case of backward between components
        field.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().closeEditor())
                .setFilter("event.key === 'Tab' && event.shiftKey");

        binder.bind(field, "itemContent");
        grid.getColumnByKey("itemContent").setEditorComponent(field);


        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            field.focus();
            service.save(binder.getBean());
        });

        grid.addItemClickListener(event -> {
            if (binder.getBean() != null) {
                notification.setText(binder.getBean().getItemContent() + ", "
                        + binder.getBean().getId());
            }

        });

        mainContent.setSizeFull();
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        add(mainContent,notificationbar, toolbar);
        setSizeFull();
        updateList();
    }


    public void updateList() {

        grid.setItems(service.findAll());

    }

}
