package com.example.jonas.firststeps.provider;

import com.example.jonas.firststeps.model.LSData;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by afe on 23.12.2015.
 */
public class FakeDataProviderImpl implements DataProvider {

    @Override
    public List<LSData> getNewData() {
        LSData data = new LSData(String.valueOf(new Date().getTime()), "1");
        LSData data2 = new LSData(String.valueOf(new Date().getTime()), "2");
        return Arrays.asList(new LSData[]{data, data2});
    }
}
