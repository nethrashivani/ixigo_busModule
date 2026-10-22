
package com.ixigo.testing.utilities;

import com.ixigo.testing.pages.BusSearchPage;
import com.ixigo.testing.pages.HomePage;
import com.ixigo.testing.pages.HomeResultPage;
import com.ixigo.testing.pages.PassengerPage;
import com.ixigo.testing.pages.SeatSelectionPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class Pages {

    public static final ThreadLocal<HomePage> lp =
            new ThreadLocal<>();

    public static final ThreadLocal<HomeResultPage> hrp =
            new ThreadLocal<>();

    public static final ThreadLocal<BusSearchPage> bsp =
            new ThreadLocal<>();

    public static final ThreadLocal<SeatSelectionPage> ssp =
            new ThreadLocal<>();

    public static final ThreadLocal<PassengerPage> pp =
            new ThreadLocal<>();

    public static void allPages(WebDriver driver) {
        lp.set(PageFactory.initElements(driver, HomePage.class));
        hrp.set(PageFactory.initElements(driver, HomeResultPage.class));
        bsp.set(PageFactory.initElements(driver, BusSearchPage.class));
        ssp.set(PageFactory.initElements(driver, SeatSelectionPage.class));
        pp.set(PageFactory.initElements(driver, PassengerPage.class));
    }

    public static void clearPages() {
        lp.remove();
        hrp.remove();
        bsp.remove();
        ssp.remove();
        pp.remove();
    }
}
