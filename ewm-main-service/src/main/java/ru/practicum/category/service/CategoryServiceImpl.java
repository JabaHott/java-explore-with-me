package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryReqDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
//    private final CategoryMapper mapper;

    @Override
    public Category create(Category category) {
        nameCheck(category.getName());
        log.info("category {} saved", category);
        return repository.save(category);
    }

    @Override
    public void delete(Long categoryId) {
        log.info("category deleted id = {}", categoryId);
        repository.deleteById(categoryId);
    }

    @Override
    public Category update(CategoryReqDto categoryReqDto, Long catId) {
        if (isNotExist(catId)) {
            log.warn("Category does not exist");
            throw new NotFoundException(catId, new Category());
        }
//        nameCheck(categoryReqDto.getName());
        Category categoryOld = repository.getReferenceById(catId);
        if (categoryReqDto.getName() != null && !(repository.existsByName(categoryReqDto.getName()))) {
            categoryOld.setName(categoryReqDto.getName());
            log.info("category {} updated", categoryOld);
            return repository.save(categoryOld);
        } else {
            return categoryOld;
        }
    }

    @Override
    public Page<Category> findAllCategory(Integer from, Integer size) {
        log.info("Получен запрос всех категорий");
        return repository.findAll(PageRequest.of(from, size));
    }

    @Override
    public Category findCategoryById(Long catId) {
        log.info("Заппрошена категория {}", catId);
        if (isNotExist(catId)) {
            String error = "Category does not exist";
            log.warn(error);
            throw new NotFoundException(catId, new Category());
        }
        return repository.getReferenceById(catId);
    }

    private boolean isNotExist(Long id) {
        return !repository.existsById(id);
    }

    private void nameCheck(String name) {
        if (repository.existsByName(name)) {
            throw new DataIntegrityViolationException("");
        }
    }
}
