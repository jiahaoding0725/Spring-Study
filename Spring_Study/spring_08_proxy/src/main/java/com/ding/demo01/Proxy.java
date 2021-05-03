package com.ding.demo01;

public class Proxy implements Rent{
    private Host host;

    public Proxy() {
    }

    public Proxy(Host host) {
        this.host = host;
    }

    public void seeHouse(){
        System.out.println("中介带你看房");
    }

    public void fee(){
        System.out.println("收中介费");
    }

    public void signContract(){
        System.out.println("签租赁合同");
    }

    public void rent() {
        seeHouse();
        host.rent();
        signContract();;
        fee();
    }
}
