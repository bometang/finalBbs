package com.KDT.mosi.domain.board.bbsUpload.dao;

import com.KDT.mosi.domain.entity.board.BbsUpload;
import com.KDT.mosi.domain.entity.board.UploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    sql.append("INSERT INTO bbs_upload(upload_id,bbs_id,file_type,sort_order,file_path,original_name,saved_name) ");
    sql.append("VALUES (bbs_upload_upload_id_seq.NEXTVAL, :bbsId, :fileType, :sortOrder, :filePath, :originalName, :savedName) ");

    SqlParameterSource param = new BeanPropertySqlParameterSource(upload);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(), param, keyHolder, new String[]{"upload_id"});

    Number key = (Number)keyHolder.getKeys().get("upload_id");
    return key.longValue();
  }

  @Override
  public List<UploadResult> saveAll(List<BbsUpload> uploads) {
    List<UploadResult> uploadResults = new ArrayList<>();
    Long id;
    for (BbsUpload upload : uploads) {
      id = save(upload);
      uploadResults.add(new UploadResult(id, upload.getFilePath()));
    }
    return uploadResults;
  }

  @Override
  public List<BbsUpload> findInlineByBbsIdOrderBySort(Long bbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * FROM bbs_upload WHERE file_type = 'INLINE' ");
    sql.append("AND bbs_id = :bbsId ");
    sql.append("ORDER BY sort_order ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("bbsId", bbsId);

    List<BbsUpload> list = template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(BbsUpload.class));

    return list;
  }

  @Override
  public List<BbsUpload> findAttachmentsByBbsIdOrderBySort(Long bbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * FROM bbs_upload WHERE file_type = 'ATTACHMENT' ");
    sql.append("AND bbs_id = :bbsId ");
    sql.append("ORDER BY sort_order ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("bbsId", bbsId);

    List<BbsUpload> list = template.query(sql.toString(), param, BeanPropertyRowMapper.newInstance(BbsUpload.class));

    return list;
  }

  @Override
  public int getMaxSortOrder(Long bbsId, String fileType) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COALESCE(MAX(sort_order), -1) FROM bbs_upload where file_type = :fileType AND bbs_id = :bbsId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("bbsId", bbsId)
        .addValue("fileType",fileType);

    int i = template.queryForObject(sql.toString(),param, Integer.class);

    return i;
  }

  @Override
  public int deleteById(Long uploadId) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM bbs_upload WHERE upload_id = :uploadId ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("uploadId",uploadId);

    int rows = template.update(sql.toString(), param);

    return rows;
  }

  @Override
  public int deleteByBbsId(Long bbsId) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM bbs_upload WHERE bbs_id = :bbsId ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("bbsId",bbsId);

    int rows = template.update(sql.toString(), param);

    return rows;
  }

  @Override
  public int updateSortOrder(Long uploadId, int sortOrder) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE bbs_upload ");
    sql.append("SET sort_order = :sortOrder ,uploaded_at = systimestamp ");
    sql.append("WHERE upload_id = :uploadId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("uploadId",uploadId)
        .addValue("sortOrder",sortOrder);

    int rows = template.update(sql.toString(), param);

    return rows;
  }

  @Override
  public int decrementSortOrders(Long bbsId, String fileType, int fromOrder) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE bbs_upload ");
    sql.append("SET sort_order = sort_order - 1 ");
    sql.append("WHERE bbs_id    = :bbsId ");
    sql.append("AND file_type = :fileType ");
    sql.append("AND sort_order > :fromOrder ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("bbsId",bbsId)
        .addValue("fileType",fileType)
        .addValue("fromOrder",fromOrder);

    int rows = template.update(sql.toString(), param);

    return rows;
  }

  @Override
  public Optional<BbsUpload> findById(Long uploadId) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * FROM bbs_upload where upload_id = :uploadId ");
    SqlParameterSource param = new MapSqlParameterSource().addValue("uploadId",uploadId);

    BbsUpload bbsUpload = null;
    try {
      bbsUpload = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(BbsUpload.class));
    } catch (EmptyResultDataAccessException e) { //template.queryForObject() : 레코드를 못찾으면 예외 발생
      return Optional.empty();
    }

    return Optional.of(bbsUpload);
  }
}
