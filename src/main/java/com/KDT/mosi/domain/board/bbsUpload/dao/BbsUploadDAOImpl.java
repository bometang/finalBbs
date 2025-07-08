package com.KDT.mosi.domain.board.bbsUpload.dao;

import com.KDT.mosi.domain.entity.board.BbsUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BbsUploadDAOImpl implements BbsUploadDAO{
  final private NamedParameterJdbcTemplate template;

  @Override
  public Long save(BbsUpload upload) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO bbs_upload(image_id,bbs_id,sort_order,file_path,original_name,saved_name) ");
    sql.append("VALUES (bbs_upload_upload_id_seq.NEXTVAL,:bbsId, :sortOrder, :filePath, :originalName, :savedName) ");

    SqlParameterSource param = new BeanPropertySqlParameterSource(upload);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(), param, keyHolder, new String[]{"image_id"});

    Number key = (Number)keyHolder.getKeys().get("image_id");
    return key.longValue();
  }

  @Override
  public List<BbsUpload> findByBbsId(Long bbsId) {
    return List.of();
  }

  @Override
  public Optional<BbsUpload> findById(Long imageId) {
    return Optional.empty();
  }

  @Override
  public boolean existsByBbsIdAndSortOrder(Long bbsId, int sortOrder) {
    return false;
  }

  @Override
  public int updateById(Long imageId, BbsUpload upload) {
    return 0;
  }

  @Override
  public int deleteByBbsId(Long bbsId) {
    return 0;
  }

  @Override
  public int deleteById(Long imageId) {
    return 0;
  }

  @Override
  public int deleteByIds(List<Long> imageIds) {
    return 0;
  }

  @Override
  public int countByBbsId(Long bbsId) {
    return 0;
  }
}
