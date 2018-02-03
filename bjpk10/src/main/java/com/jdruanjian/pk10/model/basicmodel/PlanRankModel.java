package com.jdruanjian.pk10.model.basicmodel;

import com.jdruanjian.pk10.model.entity.PlanRankBean;
import com.jdruanjian.pk10.model.entity.ResultListBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Longlong on 2018/1/12.
 */

public class PlanRankModel implements Serializable{

    public String status;
    public List<PlanRankBean> data;
}
