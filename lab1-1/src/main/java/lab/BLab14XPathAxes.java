package lab;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BLab14XPathAxes {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("http://localhost/samples/xpath-axes.php");

            // 1) child::  – all <li> children of #nav
            List<WebElement> navItems = driver.findElements(
                    By.xpath("//ul[@id='nav']/child::li"));
            System.out.println("child::li of #nav => " + navItems.size());

            // 2) descendant::  – all <li> under #content (any depth)
            List<WebElement> contentLis = driver.findElements(
                    By.xpath("//div[@id='content']/descendant::li"));
            System.out.println("descendant::li of #content => " + contentLis.size());

            // 3) parent::  – parent <ul> of the 'Products' link
            WebElement productsLinkParentUl = driver.findElement(
                    By.xpath("//a[@id='productsLink']/parent::li/parent::ul"));
            System.out.println("parent:: of #productsLink => " + productsLinkParentUl.getAttribute("id")); // nav

            // 4) ancestor::  – find #content as an ancestor of #spanInsideP2
            WebElement contentAncestor = driver.findElement(
                    By.xpath("//span[@id='spanInsideP2']/ancestor::div[@id='content']"));
            System.out.println("ancestor::div[@id='content'] of #spanInsideP2 => " + contentAncestor.getAttribute("id"));

            // 5) ancestor-or-self::  – include the span element itself
            WebElement selfOrAncestorSection1 = driver.findElement(
                    By.xpath("//span[@id='spanInsideP2']/ancestor-or-self::div[@id='section1']"));
            System.out.println("ancestor-or-self::div[@id='section1'] => " + selfOrAncestorSection1.getAttribute("id"));

            // 6) descendant-or-self:: – starting from #section1, find any <span> including itself (if it was span)
            WebElement spanFromSection1 = driver.findElement(
                    By.xpath("//div[@id='section1']/descendant-or-self::span[@id='spanInsideP2']"));
            System.out.println("descendant-or-self::span from #section1 => " + spanFromSection1.getAttribute("id"));

            // 7) following-sibling:: – items after #item1
            List<WebElement> followingSiblingsOfItem1 = driver.findElements(
                    By.xpath("//li[@id='item1']/following-sibling::li"));
            System.out.println("following-sibling::li of #item1 => " + followingSiblingsOfItem1.size());

            // 8) preceding-sibling:: – items before #item3
            List<WebElement> precedingSiblingsOfItem3 = driver.findElements(
                    By.xpath("//li[@id='item3']/preceding-sibling::li"));
            System.out.println("preceding-sibling::li of #item3 => " + precedingSiblingsOfItem3.size());

            // 9) following:: – all elements in the document after #section1
            List<WebElement> followingOfSection1 = driver.findElements(
                    By.xpath("//div[@id='section1']/following::div"));
            System.out.println("following::div of #section1 => " + followingOfSection1.size());

            // 10) preceding:: – all elements before #footer
            List<WebElement> precedingOfFooter = driver.findElements(
                    By.xpath("//div[@id='footer']/preceding::div"));
            System.out.println("preceding::div of #footer => " + precedingOfFooter.size());

            // 11) self:: – select the node itself(//div[@id='section2']/*[self::ul or self::p]), //*[@data-id][self::li]
            WebElement selfItem2 = driver.findElement(
                    By.xpath("//li[@id='item2']/self::li"));
            System.out.println("self::li of #item2 => " + selfItem2.getAttribute("id"));

        } finally {
            driver.quit();
        }
    }
}
