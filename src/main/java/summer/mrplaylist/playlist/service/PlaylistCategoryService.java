package summer.mrplaylist.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import summer.mrplaylist.playlist.model.Category;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.model.PlaylistCategory;
import summer.mrplaylist.playlist.repository.CategoryRepository;
import summer.mrplaylist.playlist.repository.PlaylistCategoryRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaylistCategoryService {

	private final CategoryRepository categoryRepository;
	private final PlaylistCategoryRepository plcRepository;

	@Transactional
	public void join(Playlist playlist, List<String> categoryNameList) {

		for (String name : categoryNameList) {
			Optional<Category> findCategory = findCategory(name);
			Category category;
			if (findCategory.isEmpty()) {
				category = createCategory(name);
			} else {
				category = findCategory.get();
			}
			PlaylistCategory playListCategory = PlaylistCategory.createPlayListCategory(playlist, category);
			plcRepository.save(playListCategory);
		}
	}

	@Transactional
	public Category createCategory(String name) {
		return categoryRepository.save(Category.createCategory(name));
	}

	public Optional<Category> findCategory(String name) {
		return categoryRepository.findByName(name);
	}
}
