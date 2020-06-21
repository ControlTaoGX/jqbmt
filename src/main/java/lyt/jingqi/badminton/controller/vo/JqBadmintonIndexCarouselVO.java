package lyt.jingqi.badminton.controller.vo;

import java.io.Serializable;

public class JqBadmintonIndexCarouselVO implements Serializable {
    private String carouselUrl;
    private String redirectUrl;

    public String getCarouselUrl() {
        return carouselUrl;
    }

    public void setCarouselUrl(String carouselUrl) {
        this.carouselUrl = carouselUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
