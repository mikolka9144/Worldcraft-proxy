package com.mikolka9144.worldcraft.programs.simba.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class SimbaFunction {
    private String name;
    private List<String> segments;
}
