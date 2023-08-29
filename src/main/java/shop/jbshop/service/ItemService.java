package shop.jbshop.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.jbshop.domain.item.Category;
import shop.jbshop.domain.item.Item;
import shop.jbshop.domain.item.ItemImg;
import shop.jbshop.dto.request.ItemSaveRequestDto;
import shop.jbshop.dto.request.ItemUpdateRequestDto;
import shop.jbshop.dto.response.AllItemResponseDto;
import shop.jbshop.dto.response.ItemResponseDto;
import shop.jbshop.dto.response.ItemUpdateResponseDto;
import shop.jbshop.repository.CartRepository;
import shop.jbshop.repository.ItemImgRepository;
import shop.jbshop.repository.ItemRepository;
import shop.jbshop.repository.MemberRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    /**
     * TODO: test
     */
    public Long registItemTest(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void saveItem(ItemSaveRequestDto dto) {
        Item item = Item.builder()
                .name(dto.getName())
                .text(dto.getText())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(Category.valueOf(dto.getCategory()))
                .build();

        String savedName = generateSaveName(dto.getImageFile().getOriginalFilename());
        String imagePath = saveImage(dto.getImageFile(), savedName);

        ItemImg itemImg = ItemImg.builder()
                .url(imagePath)
                .savedName(savedName)
                .originName(dto.getImageFile().getOriginalFilename())
                .build();
        itemImg.addItemImg(item);

        itemRepository.save(item);
    }

    private String saveImage(MultipartFile imageFile, String savedName) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("이미지파일을 제공하세요");
        }

        try {
            String savePath = "C:/CS/images/";
            String uniqueFileName = savedName;

            File directory = new File(savePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File saveFile = new File(directory, uniqueFileName);
            imageFile.transferTo(saveFile);

            return savePath + uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장중에 오류 발생");
        }
    }


    public ItemResponseDto findItem(Long itemId) {
        Optional<Item> findItem = itemRepository.findByIdAndDeletedAtNull(itemId);
        return ItemResponseDto.fromEntity(findItem);
    }

//    TODO
//    public List<AllItemResponseDto> findItems() {
//        List<Item> findItems = itemRepository.findAll();
//        return AllItemResponseDto.fromEntity(findItems);
//    }


    @Transactional
    public Long updateItem(ItemUpdateRequestDto dto) {
        Optional<Item> findItem = itemRepository.findByIdAndDeletedAtNull(dto.getItemId());
        Item item = findItem.get();
        item.updateStock(dto.getItemStock());
        item.updatePrice(dto.getItemPrice());
        item.updateName(dto.getItemName());
        item.updateText(dto.getItemText());
        return ItemUpdateResponseDto.fromEntity(Optional.of(item));
    }


    @Transactional
    public Long deleteItem(Long itemId) {
        Optional<Item> findItem = itemRepository.findByIdAndDeletedAtNull(itemId);
        Item item = findItem.get();
        item.deleteItem();
        return item.getId();
    }

    /**
     * 파일명 생성
     */
    private String generateSaveName(String originalFilename) {
        String uniqueId = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(originalFilename);
        return uniqueId + System.currentTimeMillis() + "." + fileExtension;
    }

    /**
     * 확장자 추출
     */
    private String getFileExtension(String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf('.');
        //TODO: 바꾸자
        assert dotIndex >= 0 && dotIndex < originalFilename.length() - 1;
        return originalFilename.substring(dotIndex + 1).toLowerCase();
    }


    public Page<AllItemResponseDto> findItems(Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findAll(pageable);
        return itemsPage.map(item -> AllItemResponseDto.fromEntity(item));
    }

    public Page<AllItemResponseDto> findItems(Pageable pageable, String category) {
        Page<Item> itemPage = itemRepository.findAllByCate(pageable, category);
        return itemPage.map(item -> AllItemResponseDto.fromEntity(item));
    }


    public Page<AllItemResponseDto> searchItems(Pageable pageable, String text) {
        Page<Item> itemPage = itemRepository.findAllByText(pageable, text);
        return itemPage.map(item -> AllItemResponseDto.fromEntity(item));
    }
}
