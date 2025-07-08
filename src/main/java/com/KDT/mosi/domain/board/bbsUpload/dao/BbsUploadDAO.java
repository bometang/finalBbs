package com.KDT.mosi.domain.board.bbsUpload.dao;

import com.KDT.mosi.domain.entity.board.BbsUpload;

import java.util.List;
import java.util.Optional;

public interface BbsUploadDAO {

  /** 업로드 메타데이터 INSERT 후 PK( image_id ) 반환 */
  Long save(BbsUpload upload);

  /** 게시글별( bbs_id ) 모든 이미지 조회 – sort_order 오름차순 */
  List<BbsUpload> findByBbsId(Long bbsId);

  /** image_id(PK)로 단건 조회 */
  Optional<BbsUpload> findById(Long imageId);

  /** 동일 게시글 내 sort_order 중복 여부 검사 */
  boolean existsByBbsIdAndSortOrder(Long bbsId, int sortOrder);

  /** 메타데이터 수정(예: sort_order, saved_name 등) */
  int updateById(Long imageId, BbsUpload upload);

  /** 게시글 단위 이미지 일괄 삭제 */
  int deleteByBbsId(Long bbsId);

  /** image_id 단건 삭제 */
  int deleteById(Long imageId);

  /** image_id 리스트로 여러 건 삭제 */
  int deleteByIds(List<Long> imageIds);

  /** 게시글에 등록된 이미지 총 개수 */
  int countByBbsId(Long bbsId);
}
