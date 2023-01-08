package com.lwa.dto;

import com.lwa.entity.Setmeal;
import com.lwa.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
