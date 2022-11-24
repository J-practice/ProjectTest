package com.lwa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwa.entity.Setmeal;
import com.lwa.mapper.SetmealMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements com.lwa.service.SetmealService {
}
