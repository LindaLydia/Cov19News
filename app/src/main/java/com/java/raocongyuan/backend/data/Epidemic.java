package com.java.raocongyuan.backend.data;

import java.util.Date;
import java.util.List;

public class Epidemic {
    public String country;
    public String province;
    public Date date;
    public int days;
    public List<Integer> confirmed;
    public List<Integer> cured;
    public List<Integer> dead;
}
