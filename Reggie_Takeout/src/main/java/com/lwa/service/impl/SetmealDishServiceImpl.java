package com.lwa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwa.entity.SetmealDish;
import com.lwa.mapper.SeimealDishMapper;
import com.lwa.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SeimealDishMapper, SetmealDish> implements SetmealDishService {
}
