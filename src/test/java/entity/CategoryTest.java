package entity;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CategoryTest {

    private Category category;
    private Category copy;

    @Before
    public void initCategoryAndCreatingCategoryCopy() {
        category = new Category();
        category.setName("hardware");

        List<Good> goods = new ArrayList<>();
        goods.add(new Good(1, "CPU", 500));
        goods.add(new Good(2, "GPU", 700));
        goods.add(new Good(3, "RAM", 400));

        category.setGoods(goods);

        copy = new Category(category);
    }

    @Test
    public void compareCategoryCopyWithCategoryAndThrowsErrorIfAddedNewFieldMissingInCopyConstructor() throws Exception {

        Class<Category> categoryClass = Category.class;

        Field nameField = categoryClass.getDeclaredField("name");
        Field goodsField = categoryClass.getDeclaredField("goods");

        nameField.setAccessible(true);
        goodsField.setAccessible(true);

        String categoryName = (String) nameField.get(category);
        String copyName = (String) nameField.get(copy);

        List<Good> categoryGoods = (ArrayList) goodsField.get(category);
        List<Good> copyGoods = (ArrayList) goodsField.get(copy);

        assertEquals("missing field " + nameField.getName(), categoryName, copyName);
        assertEquals("missing field " + goodsField.getName(), categoryGoods, copyGoods);
    }
}