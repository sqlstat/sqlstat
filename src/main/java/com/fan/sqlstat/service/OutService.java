package com.fan.sqlstat.service;

import com.fan.sqlstat.model.ResultSet;
import org.springframework.beans.factory.annotation.Value;

public interface OutService {

    void output(ResultSet resultSet);

    void outputAction(ResultSet resultSet);
}

