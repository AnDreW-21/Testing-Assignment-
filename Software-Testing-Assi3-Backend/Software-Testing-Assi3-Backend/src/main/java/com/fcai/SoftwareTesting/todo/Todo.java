package com.fcai.SoftwareTesting.todo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    private String id;
    private String title;
    private String description;
    private boolean completed;
    // Added this to override equal comparison while testing
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Todo todo){
            return this.id.equals(todo.id) && this.title.equals(todo.title) && this.description.equals(todo.description) && this.completed == todo.completed;
        }
        return false;
    }
}
