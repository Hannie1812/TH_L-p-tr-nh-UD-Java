package com.nbhang.entities;

import com.nbhang.validators.annotations.ValidCategoryId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 50, nullable = false)
    @Size(min = 1, max = 50, message = "Tiêu đề phải từ 1 đến 50 ký tự")
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;
    @Column(name = "author", length = 50, nullable = false)
    @Size(min = 1, max = 50, message = "Tác giả phải từ 1 đến 50 ký tự")
    @NotBlank(message = "Tác giả không được để trống")
    private String author;
    @Column(name = "price")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double price;
    @Column(name = "quantity")
    @Min(value = 0, message = "Số lượng phải từ 0 trở lên")
    private Integer quantity;
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ValidCategoryId
    @ToString.Exclude
    private Category category;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ItemInvoice> itemInvoices = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(),
                book.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}