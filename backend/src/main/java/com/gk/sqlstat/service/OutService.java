package com.gk.sqlstat.service;

import com.gk.sqlstat.model.ResultSet;

public interface OutService {

    void output(ResultSet resultSet);

    void outputAction(ResultSet resultSet);
}

