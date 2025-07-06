package com.KDT.mosi.domain.board.rbbsReport.svc;

import com.KDT.mosi.domain.entity.board.RbbsReport;
import com.KDT.mosi.domain.board.rbbsReport.dao.RBbsReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RBbsReportSVCImpl implements RBbsReportSVC {
  private final RBbsReportDAO rbbsReportDAO;

  @Override
  public String report(RbbsReport rbbsReport) {
    return rbbsReportDAO.report(rbbsReport);
  }

  @Override
  public int getTotalCountReport(Long rbbsId) {
    return rbbsReportDAO.getTotalCountReport(rbbsId);
  }
}
