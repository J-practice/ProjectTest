package com.lwa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwa.common.CustomException;
import com.lwa.entity.Category;
import com.lwa.entity.Dish;
import com.lwa.entity.Setmeal;
import com.lwa.mapper.CategoryMapper;
import com.lwa.service.CategoryService;
import com.lwa.service.DishService;
import com.lwa.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            //已经关联了菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count();
        if (count2 > 0) {
            //已经关联了套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}
