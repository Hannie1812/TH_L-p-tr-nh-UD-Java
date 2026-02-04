package com.nbhang.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", length = 50, nullable = false)
    @Size(min = 1, max = 50, message = "Tên danh mục phải từ 1 đến 50 ký tự")
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(),
                category.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}