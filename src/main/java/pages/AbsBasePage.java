package pages;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import exceptions.PathEmptyException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobject.AbsPageObject;

public abstract class AbsBasePage<T> extends AbsPageObject {

    private String BASE_URL = System.getProperty("base.url");

//    {
//        waiters.waitForCondition(ExpectedConditions.visibilityOf());
//    }
    public AbsBasePage(WebDriver driver) {
        super(driver);
    }
    private String getPath() throws PathEmptyException{
        Class<? extends AbsBasePage> clazz = this.getClass();
        if(clazz.isAnnotationPresent(Path.class)){
            Path path =clazz.getAnnotation(Path.class);
            return path.value();
        }

        throw new PathEmptyException();
    }
    public T open() throws PathEmptyException{
        String path = getPath();
        String url = BASE_URL + "/" + path;
        if(BASE_URL.endsWith("/")){
            url = BASE_URL + "/" + path;
        }
        driver.get(url);
        return (T)this;
    }

    public T open(String name, String[] params){
        Class<? extends AbsBasePage> clazz = this.getClass();
        if(clazz.isAnnotationPresent(UrlTemplates.class)){
            UrlTemplates urlTemplates = clazz.getAnnotation(UrlTemplates.class);
            Template[] templates = urlTemplates.templates();

            for (Template template:templates){
                if(template.name().equals(name)){
                    String urlTemplate = template.template();
                    for (int i=0; i < params.length;i++){
                        urlTemplate=urlTemplate.replace(String.format("{%d}", i+1), params[i]);
                    }
                    driver.get(BASE_URL + urlTemplate);
                }
            }
        }
        return (T) this;
    }
}