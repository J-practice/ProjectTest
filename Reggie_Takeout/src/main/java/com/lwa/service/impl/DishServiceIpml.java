package com.lwa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwa.entity.Dish;
import com.lwa.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishServiceIpml extends ServiceImpl<DishMapper, Dish> implements com.lwa.service.DishService {
}
