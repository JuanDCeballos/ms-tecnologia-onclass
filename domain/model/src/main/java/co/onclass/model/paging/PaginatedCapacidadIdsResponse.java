package co.onclass.model.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedCapacidadIdsResponse {

    private long totalElementos;
    private List<Long> ids;
}
