import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class Main {
        private static List<String[]> data = new ArrayList<>();
//        private static List<String> caseName = asList("Snakebite", "Prisma", "Prisma%202", "Fracture", "Danger%20Zone", "CS20", "Horizon", "Shadow", "Falchion",
//                "Clutch", "Recoil", "Dreams%20%26%20Nightmares", "Revolver", "Spectrum%202");

        //"Snakebite", "Prisma", "Prisma%202", "Fracture", "Danger%20Zone", "CS20", "Horizon", "Shadow", "Falchion",
        //                "Clutch", "Recoil", "Dreams%20%26%20Nightmares", "Revolver", "Spectrum%202", "Chroma%203", "Gamma%202", "Spectrum", "Operation%20Vanguard%20Weapon",
        //                "Chroma%202", "Operation%20Phoenix%20Weapon", "Revolution", "Chroma", "Shattered%20Web", "Operation%20Broken%20Fang", "Operation%20Riptide",
        //                "Glove%20", "Operation%20Breakout%20Weapon", "eSports%202014%20Summer", "eSports%202013%20Winter", "Winter%20Offensive%20Weapon", "Huntsman%20Weapon",
        //                "Operation%20Hydra", "eSports%202013", "Operation%20Bravo", "CS%3AGO%20Weapon"
        private static DateFormat dateFormat = new SimpleDateFormat("dd.MM: HH:mm:ss");
        private static Date date = new Date();

        public static void main(String[] args) throws InterruptedException {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            WebDriver driver = new ChromeDriver(options);

            for(int i = 1; i < 5; i++) {
                String url = "https://steamcommunity.com/market/search?q=case&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=any&category_730_Type%5B%5D=tag_CSGO_Type_WeaponCase&appid=730#p" + i + "_price_asc";
                driver.get(url);
                Thread.sleep(1000);

                List<WebElement> cases = driver.findElements(By.className("market_listing_row_link"));
                for (WebElement caseItem : cases) {
                    String caseName;
                    String casePrice;
                    String caseCount;
                    while (true) {

                        try {
                            WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.className("market_listing_item_name")));
                        } catch (Exception e){
                            driver.quit();
                        }
                        caseName = caseItem.findElement(By.className("market_listing_item_name")).getText();

                        
                        try {
                            WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.className("normal_price")));
                        } catch (Exception e){
                            driver.quit();
                        }
                        casePrice = caseItem.findElement(By.className("normal_price")).getText();


                        try {
                            WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.className("market_listing_num_listings_qty")));
                        } catch (Exception e){
                            driver.quit();
                        }
                        caseCount = caseItem.findElement(By.className("market_listing_num_listings_qty")).getText();


                        if (casePrice.length() > 0 && caseName.length() > 0) {
                            break;
                        }
                    }
                    addData(caseName, casePrice, caseCount);
                }

            }
            printData();
            driver.quit();
        }

        private static void addData(String name, String price, String count) {
            StringBuilder priceChange = new StringBuilder(price);
            StringBuilder countChange = new StringBuilder();
            int check = 0;
            char c;
            while(true){
                c = priceChange.charAt(check);
                if(!(Character.isDigit(c)) && c != '.'){
                    priceChange.deleteCharAt(check);
                } else check++;
                if(check == priceChange.length()) break;
            }
            priceChange.append("$");
            String[] row = {name,  count, priceChange.toString(), dateFormat.format(date)};
            data.add(row);
        }

        private static void printData() {
            for (String[] row : data) {
                System.out.println("Name - " + row[0] + ", Count - " + row[1] + ", Price - " + row[2] + ", Date - " + row[3]);
            }
        }
}